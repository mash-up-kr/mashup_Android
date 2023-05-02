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
            RankingUiState.EmptyRanking()
        )
    val personalRanking = _personalRanking.asStateFlow()

    private val _platformRanking:
        MutableStateFlow<RankingUiState> = MutableStateFlow(RankingUiState.MyPlatformRanking())
    val platformRanking = _platformRanking.asStateFlow()

    private val userPreference: MutableStateFlow<UserPreference> = MutableStateFlow(
        UserPreference.getDefaultInstance()
    )

    init {
        mashUpScope {
            updateAllRankingList()
            updatePlatformRanking()
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
                        text = it.memberName,
                        totalShakeScore = it.totalShakeScore
                    )
                } ?: RankingUiState.EmptyRanking()
            }
            _mashUpRankingList.update { elevenRankingList }
        }
    }

    /**
     * 플랫폼 랭킹을 얻어와 내 플랫폼 랭킹까지 업데이트 합니다.
     */
    internal suspend fun updatePlatformRanking() {
        val platformRankingResult = danggnRepository.getPlatformDanggnRank(GENERATION_NUMBER)
        if (platformRankingResult.isSuccess()) {
            val platformRankingList = platformRankingResult.data ?: emptyList()
            val sixPlatformRankingList = (0..5).map { index ->
                platformRankingList.getOrNull(index)?.let {
                    RankingUiState.PlatformRanking(
                        memberId = it.platform,
                        text = it.platform,
                        totalShakeScore = it.totalShakeScore
                    )
                } ?: RankingUiState.EmptyRanking()
            }
            setMyPlatformRanking(sixPlatformRankingList)
            _platformRankingList.update { sixPlatformRankingList }
        }
    }

    private fun setMyPlatformRanking(sixPlatformRankingList: List<RankingUiState>) {
        val platformName = userPreference.value.platform.detailName
        val matchedItemIndex = sixPlatformRankingList.indexOfFirst { matched ->
            if (matched is RankingUiState.MyPlatformRanking) {
                matched.text == platformName
            } else {
                false
            }
        }
        _platformRanking.value = RankingUiState.MyPlatformRanking(
            memberId = platformName,
            text = matchedItemIndex.takeIf { number -> number > 0 }?.let { num ->
                "${num}위"
            } ?: "",
            totalShakeScore = kotlin.runCatching { sixPlatformRankingList[matchedItemIndex].totalShakeScore }
                .getOrNull() ?: DEFAULT_SHAKE_NUMBER
        )
    }

    /**
     * 개인 랭킹(크루원, 플랫폼)을 얻어옵니다
     */
    internal suspend fun updatePersonalRanking() {
        val personalRanking = danggnRepository.getPersonalDanggnRank(GENERATION_NUMBER)
        if (personalRanking.isSuccess()) {
            _personalRanking.value = personalRanking.data?.let {
                RankingUiState.MyRanking(
                    memberId = it.memberId.toString(),
                    totalShakeScore = it.totalShakeScore,
                    text = mashUpRankingList.value.indexOfFirst { matched ->
                        matched.memberId == it.memberId.toString()
                    }.takeIf { number -> number > 0 }?.let { num ->
                        "${num}위"
                    } ?: "",
                )
            } ?: RankingUiState.EmptyRanking()
        }
    }

    internal suspend fun updateUserPreference() {
        userPreference.value = userPreferenceRepository.getUserPreference().firstOrNull()
            ?: UserPreference.getDefaultInstance()
    }


    sealed interface RankingUiState {

        val text: String
        val memberId: String
        val totalShakeScore: Int

        /**
         * 크루원 랭킹 Item
         */
        data class Ranking(
            override val memberId: String = "",
            override val text: String = "",
            override val totalShakeScore: Int = DEFAULT_SHAKE_NUMBER
        ) : RankingUiState

        data class EmptyRanking(
            override val memberId: String = UUID.randomUUID().toString(),
            override val text: String = "",
            override val totalShakeScore: Int = DEFAULT_SHAKE_NUMBER,
        ) : RankingUiState

        /**
         * 플랫폼 랭킹 아이템
         */
        data class PlatformRanking(
            override val memberId: String = UUID.randomUUID().toString(),
            override val text: String = "",
            override val totalShakeScore: Int = DEFAULT_SHAKE_NUMBER,
        ) : RankingUiState

        data class MyRanking(
            override val memberId: String = "",
            override val totalShakeScore: Int = DEFAULT_SHAKE_NUMBER,
            override val text: String = "",
        ) : RankingUiState

        data class MyPlatformRanking(
            override val text: String = "",
            override val memberId: String = UUID.randomUUID().toString(),
            override val totalShakeScore: Int = DEFAULT_SHAKE_NUMBER
        ) : RankingUiState
    }
}