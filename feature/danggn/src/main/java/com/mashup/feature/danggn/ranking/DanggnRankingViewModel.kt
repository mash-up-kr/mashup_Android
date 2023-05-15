package com.mashup.feature.danggn.ranking

import androidx.lifecycle.viewModelScope
import com.mashup.core.common.base.BaseViewModel
import com.mashup.core.model.data.local.DanggnPreference
import com.mashup.core.model.data.local.UserPreference
import com.mashup.datastore.data.repository.DanggnPreferenceRepository
import com.mashup.datastore.data.repository.UserPreferenceRepository
import com.mashup.feature.danggn.data.repository.DanggnRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.UUID
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class DanggnRankingViewModel @Inject constructor(
    private val danggnRepository: DanggnRepository,
    private val userPreferenceRepository: UserPreferenceRepository,
    private val danggnPreferenceRepository: DanggnPreferenceRepository
) : BaseViewModel() {
    companion object {
        private const val GENERATION_NUMBER = 13
        private const val DEFAULT_SHAKE_NUMBER = -1
    }

    private val personalRankingList: MutableStateFlow<List<RankingItem>> =
        MutableStateFlow(
            emptyList()
        )

    private val platformRankingList: MutableStateFlow<List<RankingItem>> =
        MutableStateFlow(
            emptyList()
        )

    private val currentTabIndex = MutableStateFlow(0)

    val uiState: StateFlow<RankingUiState> = combine(
        currentTabIndex,
        userPreferenceRepository.getUserPreference(),
        danggnPreferenceRepository.getDanggnPreference(),
        personalRankingList,
        platformRankingList
    ) { tabIndex, userPreference, danggnPreference, personalRankingList, platformRankingList ->
        RankingUiState(
            firstPlaceState = getFirstPlaceState(
                tabIndex, userPreference, danggnPreference, personalRankingList, platformRankingList
            ),
            personalRankingList = createPersonalRankingList(personalRankingList),
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

    private fun createPersonalRankingList(personalRankingList: List<RankingItem>): List<RankingItem> {
        return (0..10).map { index ->
            personalRankingList.getOrNull(index) ?: RankingItem.EmptyRanking()
        }
    }

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    init {
        getRankingData()
    }

    fun refreshRankingData() {
        mashUpScope {
            _isRefreshing.value = true
            getRankingData()
            delay(1000L)
            _isRefreshing.value = false
        }
    }

    internal fun getRankingData() {
        mashUpScope {
            updateAllRankingList()
            updatePlatformRanking()
        }
    }

    internal fun updateCurrentTabIndex(index: Int) = mashUpScope {
        currentTabIndex.emit(index)
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
            val allRankingList = rankingList.map {
                    RankingItem.Ranking(
                        memberId = it.memberId.toString(),
                        text = it.memberName,
                        totalShakeScore = it.totalShakeScore
                    )
                }
            personalRankingList.emit(allRankingList)
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
        val myPersonalRank = personalRankingList.find { it.memberId == userPreference.id.toString() }
            ?: return RankingItem.EmptyRanking()
        val index = personalRankingList.indexOf(myPersonalRank)

        return RankingItem.MyRanking(
            memberId = myPersonalRank.memberId,
            totalShakeScore = myPersonalRank.totalShakeScore,
            text = "${index + 1}위"
        )
    }

    private fun getFirstPlaceState(
        tabIndex: Int,
        userPreference: UserPreference,
        danggnPreference: DanggnPreference,
        personalRankingList: List<RankingItem>,
        platformRankingList: List<RankingItem>
    ): FirstRankingState {
        val myName = userPreference.name
        val myPlatform = userPreference.platform.detailName

        val currentPersonalRanking = personalRankingList.indexOfFirst { it.text == myName }
        val currentPlatformRanking = platformRankingList.indexOfFirst { it.text == myPlatform }

        if (
            tabIndex == 0 && currentPersonalRanking == -1 ||
            tabIndex == 1 && currentPlatformRanking == -1
        ) {
            return FirstRankingState.Empty
        }

        if (
            tabIndex == 0 && danggnPreference.personalFirstRanking == currentPersonalRanking ||
            tabIndex == 1 && danggnPreference.platformFirstRanking == currentPlatformRanking
        ) {
            updateFirstRanking()
            return FirstRankingState.Empty
        }

        return when {
            tabIndex == 0 && currentPersonalRanking == 0 -> {
                FirstRankingState.FirstRanking("${myName}님")
            }
            tabIndex == 1 && currentPlatformRanking == 0 -> {
                FirstRankingState.FirstRanking("$myPlatform 팀")
            }
            else -> {
                updateFirstRanking()
                FirstRankingState.Empty
            }
        }
    }

    internal fun updateFirstRanking() = mashUpScope {
        val userPreference = userPreferenceRepository.getUserPreference().first()
        val myName = userPreference.name
        val myPlatform = userPreference.platform.detailName
        if (currentTabIndex.value == 0) {
            danggnPreferenceRepository.updatePersonalFirstRanking(
                ranking = personalRankingList.value.indexOfFirst { it.text == myName }
            )
        } else {
            danggnPreferenceRepository.updatePlatformFirstRanking(
                ranking = platformRankingList.value.indexOfFirst { it.text == myPlatform }
            )
        }
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

    sealed interface FirstRankingState {
        object Empty : FirstRankingState
        data class FirstRanking(val text: String) : FirstRankingState
    }
}

data class RankingUiState(
    val firstPlaceState: DanggnRankingViewModel.FirstRankingState = DanggnRankingViewModel.FirstRankingState.Empty,
    val personalRankingList: List<DanggnRankingViewModel.RankingItem> = emptyList(),
    val platformRankingList: List<DanggnRankingViewModel.RankingItem> = emptyList(),
    val myPersonalRanking: DanggnRankingViewModel.RankingItem = DanggnRankingViewModel.RankingItem.EmptyRanking(),
    val myPlatformRanking: DanggnRankingViewModel.RankingItem = DanggnRankingViewModel.RankingItem.EmptyRanking()
)