package com.mashup.ui.attendance.member

import androidx.lifecycle.viewModelScope
import com.mashup.core.common.base.BaseViewModel
import com.mashup.data.repository.MemberRepository
import com.mashup.feature.mypage.profile.model.ProfileCardData
import com.mashup.feature.mypage.profile.model.ProfileData
import com.mashup.ui.mypage.MyProfileMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MemberInfoViewModel @Inject constructor(
    private val memberRepository: MemberRepository,
    private val mapper: MyProfileMapper
) : BaseViewModel() {
    private val memberScoreInfo = MutableStateFlow<Double?>(null)
    private val memberGenerationInfo = MutableStateFlow<List<ProfileCardData>?>(null)
    private val memberProfileInfo = MutableStateFlow<ProfileData?>(null)

    val memberInfo = combine(
        memberScoreInfo,
        memberGenerationInfo,
        memberProfileInfo
    ) { score, generationList, profile ->
        if (score != null && generationList != null && profile != null) {
            MemberInfoModel(score, generationList, profile)
        } else {
            null
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        null
    )

    fun getMemberInfo(name: String, memberId: String) {
        memberScoreInfo.value = null
        memberGenerationInfo.value = null
        memberProfileInfo.value = null

        getMemberProfile(name, memberId)
        getMemberScore(memberId)
    }

    private fun getMemberProfile(name: String, memberId: String) {
        mashUpScope {
            val result = memberRepository.getMemberProfile(memberId)

            result.data?.let { memberProfileResponse ->
                memberProfileInfo.value = mapper.mapToProfileData(memberProfileResponse)
                memberGenerationInfo.value = memberProfileResponse.memberGenerations.map {
                    mapper.mapToProfileCardData(name, it)
                }
            }
        }
    }

    private fun getMemberScore(memberId: String) {
        mashUpScope {
            val result = memberRepository.getMemberScore(memberId)
            memberScoreInfo.value = result.data?.totalScore
        }
    }
}
