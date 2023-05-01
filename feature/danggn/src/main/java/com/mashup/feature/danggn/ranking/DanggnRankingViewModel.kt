package com.mashup.feature.danggn.ranking

import com.mashup.core.common.base.BaseViewModel
import com.mashup.core.model.data.local.UserPreference
import com.mashup.datastore.data.repository.UserPreferenceRepository
import com.mashup.feature.danggn.data.repository.DanggnRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class DanggnRankingViewModel @Inject constructor(
    private val danggnRepository: DanggnRepository,
    private val userPreferenceRepository: UserPreferenceRepository
) : BaseViewModel() {
    companion object {
        private const val GENERATION_NUMBER = 13
        private const val DEFAULT_SHAKE_NUMBER = -1
    }

    private val _mashUpRankingList: MutableStateFlow<List<RankingUiState>> =
        MutableStateFlow(
            emptyList()
        )
    val mashUpRankingList = _mashUpRankingList.asStateFlow()

    private val _platformRankingList: MutableStateFlow<List<RankingUiState>> =
        MutableStateFlow(
            emptyList()
        )
    val platformRankingList = _platformRankingList.asStateFlow()

    private val _personalRanking: MutableStateFlow<RankingUiState> =
        MutableStateFlow(
            RankingUiState.EmptyRanking(
                name = "",
                totalShakeScore = 0
            )
        )
    val personalRanking = _personalRanking.asStateFlow()

    private val _userPreference: MutableStateFlow<UserPreference> = MutableStateFlow(
        UserPreference.getDefaultInstance()
    )
    val userPreference = _userPreference.asStateFlow()

    init {
        mashUpScope {
            updateAllRankingList()
            updatePlatformRankingList()
            updatePersonalRanking()
            updateUserPreference()
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
            val elevenRankingList = (0..10).map { index ->
                rankingList.getOrNull(index)?.let {
                    RankingUiState.Ranking(
                        memberId = it.memberId.toString(),
                        memberName = it.memberName,
                        totalShakeScore = it.totalShakeScore
                    )
                } ?: RankingUiState.EmptyRanking()
            }
            _mashUpRankingList.update { elevenRankingList }
        }
    }

    /**
     * 플랫폼 랭킹을 얻어옵니다
     */
    internal suspend fun updatePlatformRankingList() {
        val platformRankingResult = danggnRepository.getPlatformDanggnRank(GENERATION_NUMBER)
        if (platformRankingResult.isSuccess()) {
            val platformRankingList = platformRankingResult.data ?: emptyList()
            val sixPlatformRankingList = (0..5).map { index ->
                platformRankingList.getOrNull(index)?.let {
                    RankingUiState.PlatformRanking(
                        platformName = it.platform,
                        totalShakeScore = it.totalShakeScore
                    )
                } ?: RankingUiState.EmptyRanking()
            }
            _platformRankingList.update { sixPlatformRankingList }
        }
    }

    /**
     * 개인 랭킹을 얻어옵니다
     */
    internal suspend fun updatePersonalRanking() {
        val personalRanking = danggnRepository.getPersonalDanggnRank(GENERATION_NUMBER)
        if (personalRanking.isSuccess()) {
            _personalRanking.value = personalRanking.data?.let {
                RankingUiState.Ranking(
                    memberId = it.memberId.toString(),
                    memberName = it.memberName,
                    totalShakeScore = it.totalShakeScore
                )
            } ?: RankingUiState.EmptyRanking()
        }
    }

    internal suspend fun updateUserPreference() {
        _userPreference.value = userPreferenceRepository.getUserPreference().firstOrNull()
            ?: UserPreference.getDefaultInstance()
    }


    sealed interface RankingUiState {

        val memberId: String
        val totalShakeScore: Int

        data class Ranking(
            override val memberId: String = "",
            val memberName: String = "",
            override val totalShakeScore: Int = DEFAULT_SHAKE_NUMBER
        ) : RankingUiState

        data class EmptyRanking(
            override val memberId: String = UUID.randomUUID().toString(),
            val name: String = "",
            override val totalShakeScore: Int = DEFAULT_SHAKE_NUMBER,
        ) : RankingUiState

        data class PlatformRanking(
            override val memberId: String = UUID.randomUUID().toString(),
            val platformName: String = "",
            override val totalShakeScore: Int = DEFAULT_SHAKE_NUMBER,
        ) : RankingUiState
    }
}