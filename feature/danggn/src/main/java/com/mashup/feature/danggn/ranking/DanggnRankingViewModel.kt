package com.mashup.feature.danggn.ranking

import com.mashup.core.common.base.BaseViewModel
import com.mashup.feature.danggn.data.dto.DanggnMemberRankResponse
import com.mashup.feature.danggn.data.dto.DanggnPlatformRankResponse
import com.mashup.feature.danggn.data.repository.DanggnRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class DanggnRankingViewModel @Inject constructor(
    private val danggnRepository: DanggnRepository
) : BaseViewModel() {
    companion object {
        private const val GENERATION_NUMBER = 13
        private const val DEFAULT_ID = -1
    }

    private val _mashUpRankingList: MutableStateFlow<List<DanggnMemberRankResponse>> =
        MutableStateFlow(
            emptyList()
        )
    val mashUpRankingList = _mashUpRankingList.asStateFlow()

    private val _platformRankingList: MutableStateFlow<List<DanggnPlatformRankResponse>> =
        MutableStateFlow(
            emptyList()
        )
    val platformRankingList = _platformRankingList.asStateFlow()

    private val _personalRanking: MutableStateFlow<DanggnMemberRankResponse> =
        MutableStateFlow(
            DanggnMemberRankResponse(
                memberId = -1,
                memberName = "",
                totalShakeScore = 0
            )
        )
    val personalRanking = _personalRanking.asStateFlow()

    init {
        mashUpScope {
            updateAllRankingList()
            updatePlatformRankingList()
            updatePersonalRanking()
        }
    }

    /**
     * 밖에서도 호출할 수 있도록 private가 아닌 internal로 만들었습니다.
     * TODO else 처리에 대해서 논의해봐야할 것 같습니다.
     */

    /**
     * 모든 멤버의 랭킹 리스트를 얻어옵니다 (11개)
     */
    internal suspend fun updateAllRankingList() {
        val allMemberRankingResult = danggnRepository.getAllDanggnRank(GENERATION_NUMBER)
        if (allMemberRankingResult.isSuccess()) {
            val rankingList = allMemberRankingResult.data?.allMemberRankList ?: listOf()
            _mashUpRankingList.update { rankingList }
        }
    }

    /**
     * 플랫폼 랭킹을 얻어옵니다
     */
    internal suspend fun updatePlatformRankingList() {
        val platformRankingResult = danggnRepository.getPlatformDanggnRank(GENERATION_NUMBER)
        if (platformRankingResult.isSuccess()) {
            _platformRankingList.value = platformRankingResult.data ?: emptyList()
        }
    }

    /**
     * 개인 랭킹을 얻어옵니다
     */
    internal suspend fun updatePersonalRanking() {
        val personalRanking = danggnRepository.getPersonalDanggnRank(GENERATION_NUMBER)
        if (personalRanking.isSuccess()) {
            _personalRanking.value = personalRanking.data ?: DanggnMemberRankResponse(
                memberId = DEFAULT_ID,
                memberName = "",
                totalShakeScore = 0
            )
        }
    }
}