package com.mashup.feature.danggn.ranking

import androidx.lifecycle.viewModelScope
import com.mashup.core.common.base.BaseViewModel
import com.mashup.core.model.data.local.UserPreference
import com.mashup.datastore.data.repository.UserPreferenceRepository
import com.mashup.feature.danggn.data.repository.DanggnRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class DanggnRankingViewModel @Inject constructor(
    private val danggnRepository: DanggnRepository,
    userPreferenceRepository: UserPreferenceRepository
) : BaseViewModel() {
    companion object {
        private const val GENERATION_NUMBER = 13
        private const val DEFAULT_SHAKE_NUMBER = -1
    }

    private val userPreferenceFlow = userPreferenceRepository.getUserPreference()

    private val personalRankingList: MutableStateFlow<List<RankingItem>> =
        MutableStateFlow(
            emptyList()
        )

    private val platformRankingList: MutableStateFlow<List<RankingItem>> =
        MutableStateFlow(
            emptyList()
        )

    val uiState: StateFlow<RankingUiState> = combine(
        userPreferenceFlow, personalRankingList, platformRankingList
    ) { userPreference, personalRankingList, platformRankingList ->
        RankingUiState(
            personalRankingList = personalRankingList,
            platformRankingList = platformRankingList,
            myPersonalRanking = getPersonalRankingItem(
                userPreference = userPreference,
                personalRankingList = personalRankingList
            ),
            myPlatformRanking = getPlatformRankingItem(
                userPreference = userPreference,
                platformRankingList = platformRankingList
            )
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        RankingUiState()
    )

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    init {
        getRankingData()
    }

    fun getRankingData() {
        mashUpScope {
            _isRefreshing.value = true
            updateAllRankingList()
            updatePlatformRanking()
            _isRefreshing.value = false
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
                    RankingItem.Ranking(
                        memberId = it.memberId.toString(),
                        text = it.memberName,
                        totalShakeScore = it.totalShakeScore
                    )
                } ?: RankingItem.EmptyRanking()
            }
            personalRankingList.emit(elevenRankingList)
        }
    }

    /**
     * 플랫폼 랭킹을 얻어와 내 플랫폼 랭킹까지 업데이트 합니다.
     */
    internal suspend fun updatePlatformRanking() {
        val result = danggnRepository.getPlatformDanggnRank(GENERATION_NUMBER)
        if (result.isSuccess()) {
            val sixPlatformRankingList = (0..5).map { index ->
                result.data?.getOrNull(index)?.let {
                    RankingItem.PlatformRanking(
                        memberId = it.platform.detailName,
                        text = it.platform.detailName,
                        totalShakeScore = it.totalShakeScore
                    )
                } ?: RankingItem.EmptyRanking()
            }
            platformRankingList.emit(sixPlatformRankingList)
        }
    }

    internal fun getPlatformRankingItem(
        userPreference: UserPreference,
        platformRankingList: List<RankingItem>
    ): RankingItem {
        val matchedItemIndex = platformRankingList.indexOfFirst { matched ->
            matched.text == userPreference.platform.detailName
        }
        if (matchedItemIndex == -1) return RankingItem.EmptyRanking()
        return RankingItem.MyPlatformRanking(
            memberId = userPreference.platform.detailName,
            text = "${matchedItemIndex + 1}위",
            totalShakeScore = kotlin.runCatching { platformRankingList[matchedItemIndex].totalShakeScore }
                .getOrNull() ?: DEFAULT_SHAKE_NUMBER
        )
    }

    /**
     * 개인 랭킹(크루원, 플랫폼)을 얻어옵니다
     */
    internal fun getPersonalRankingItem(
        userPreference: UserPreference,
        personalRankingList: List<RankingItem>
    ): RankingItem {
        val myPersonalRank = personalRankingList.find { it.text == userPreference.name }
            ?: return RankingItem.EmptyRanking()
        val index = personalRankingList.indexOf(myPersonalRank)

        return RankingItem.MyRanking(
            memberId = myPersonalRank.memberId,
            totalShakeScore = myPersonalRank.totalShakeScore,
            text = "${index + 1}위"
        )
    }

    sealed interface RankingItem {

        val text: String
        val memberId: String
        val totalShakeScore: Int

        /**
         * 크루원 랭킹 Item
         */
        data class Ranking(
            override val memberId: String = "",
            override val text: String = "",
            override val totalShakeScore: Int = DEFAULT_SHAKE_NUMBER,
        ) : RankingItem

        data class EmptyRanking(
            override val memberId: String = UUID.randomUUID().toString(),
            override val text: String = "",
            override val totalShakeScore: Int = DEFAULT_SHAKE_NUMBER,
        ) : RankingItem

        /**
         * 플랫폼 랭킹 아이템
         */
        data class PlatformRanking(
            override val memberId: String = UUID.randomUUID().toString(),
            override val text: String = "",
            override val totalShakeScore: Int = DEFAULT_SHAKE_NUMBER,
        ) : RankingItem

        data class MyRanking(
            override val memberId: String = "",
            override val totalShakeScore: Int = DEFAULT_SHAKE_NUMBER,
            override val text: String = "",
        ) : RankingItem

        data class MyPlatformRanking(
            override val text: String = "",
            override val memberId: String = UUID.randomUUID().toString(),
            override val totalShakeScore: Int = DEFAULT_SHAKE_NUMBER,
        ) : RankingItem
    }
}

data class RankingUiState(
    val personalRankingList: List<DanggnRankingViewModel.RankingItem> = emptyList(),
    val platformRankingList: List<DanggnRankingViewModel.RankingItem> = emptyList(),
    val myPersonalRanking: DanggnRankingViewModel.RankingItem = DanggnRankingViewModel.RankingItem.EmptyRanking(),
    val myPlatformRanking: DanggnRankingViewModel.RankingItem = DanggnRankingViewModel.RankingItem.EmptyRanking()
)