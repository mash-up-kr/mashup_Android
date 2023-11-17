package com.mashup.ui.attendance.member

import androidx.lifecycle.viewModelScope
import com.mashup.core.common.base.BaseViewModel
import com.mashup.feature.mypage.profile.model.ProfileCardData
import com.mashup.feature.mypage.profile.model.ProfileData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class MemberInfoViewModel: BaseViewModel() {
    private val memberScoreInfo = MutableStateFlow<Double?>(null)
    private val memberGenerationInfo = MutableStateFlow<List<ProfileCardData>?>(null)
    private val memberProfileInfo = MutableStateFlow<ProfileData?>(null)

    val memberInfo = combine(memberScoreInfo, memberGenerationInfo, memberProfileInfo) { score, generationList, profile ->
        if (score != null && generationList != null && profile != null) {
            MemberInfoModel(
                score = score, generationList = generationList, profile = profile
            )
        } else {
            null
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        null
    )
}
