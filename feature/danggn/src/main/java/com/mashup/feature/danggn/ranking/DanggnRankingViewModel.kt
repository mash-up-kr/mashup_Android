package com.mashup.feature.danggn.ranking

import android.annotation.SuppressLint
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.mashup.core.common.base.BaseViewModel
import com.mashup.datastore.model.DanggnPreference
import com.mashup.datastore.model.UserPreference
import com.mashup.core.common.constant.BAD_REQUEST
import com.mashup.core.common.extensions.combineWithSevenValue
import com.mashup.core.common.extensions.suspendRunCatching
import com.mashup.core.common.utils.TimerUtils
import com.mashup.core.common.utils.trip
import com.mashup.core.data.repository.PopUpRepository
import com.mashup.core.data.repository.StorageRepository
import com.mashup.core.ui.widget.MashUpPopupEntity
import com.mashup.datastore.data.repository.DanggnPreferenceRepository
import com.mashup.datastore.data.repository.UserPreferenceRepository
import com.mashup.feature.danggn.constant.EXTRA_SHOW_DANGGN_REWARD_NOTICE
import com.mashup.feature.danggn.data.dto.DanggnRankingSingleRoundCheckResponse
import com.mashup.feature.danggn.data.repository.DanggnRepository
import com.mashup.feature.danggn.reward.model.DanggnPopupType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.withContext
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class DanggnRankingViewModel @Inject constructor(
    private val danggnRepository: DanggnRepository,
    private val popupRepository: PopUpRepository,
    private val storageRepository: StorageRepository,
    private val userPreferenceRepository: UserPreferenceRepository,
    private val danggnPreferenceRepository: DanggnPreferenceRepository,
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel() {
    companion object {
        private const val DEFAULT_SHAKE_NUMBER = -1
        private const val DATE_UNIT = 60 * 60 * 24 * 1000
    }

    private val _allDanggnRoundList: MutableStateFlow<List<AllRound>> =
        MutableStateFlow(emptyList())
    val allDanggnRoundList: StateFlow<List<AllRound>> = _allDanggnRoundList.asStateFlow()

    private val _currentRoundId: MutableStateFlow<Int> = MutableStateFlow(-1)
    val currentRoundId: StateFlow<Int> = _currentRoundId.asStateFlow()

    private val _currentRound = MutableStateFlow<DanggnRankingSingleRoundCheckResponse?>(null)
    val currentRound: StateFlow<DanggnRankingSingleRoundCheckResponse?> = _currentRound.asStateFlow()

    private val personalRankingList = MutableStateFlow<List<RankingItem>>(emptyList())

    private val platformRankingList = MutableStateFlow<List<RankingItem>>(emptyList())

    private val timer = TimerUtils()
    private val timerCount: MutableStateFlow<RankingItem.Timer> =
        MutableStateFlow(RankingItem.Timer(""))

    private val currentTabIndex = MutableStateFlow(0)

    private val _showLastRoundRewardPopup = MutableSharedFlow<Pair<String, MashUpPopupEntity>?>()
    val showLastRoundRewardPopup = _showLastRoundRewardPopup.asSharedFlow()

    val uiState: StateFlow<RankingUiState> = combineWithSevenValue(
        currentTabIndex,
        userPreferenceRepository.getUserPreference(),
        danggnPreferenceRepository.getDanggnPreference(),
        personalRankingList,
        platformRankingList,
        allDanggnRoundList,
        timerCount
    ) { tabIndex, userPreference, danggnPreferenceRepository, personalRankingList, platformRankingList, allDanggnRoundList, timer ->
        RankingUiState(
            firstPlaceState = getFirstPlaceState(
                tabIndex,
                userPreference,
                danggnPreferenceRepository,
                personalRankingList,
                platformRankingList
            ),
            personalRankingList = personalRankingList,
            platformRankingList = platformRankingList,
            myPersonalRanking = getPersonalRankingItem(
                userPreference = userPreference,
                personalRankingList = personalRankingList
            ),
            myPlatformRanking = getPlatformRankingItem(
                userPreference = userPreference,
                platformRankingList = platformRankingList
            ),
            danggnAllRoundList = allDanggnRoundList,
            timer = timer
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        RankingUiState()
    )

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    val showRewardNoticeDialog = savedStateHandle.getStateFlow(EXTRA_SHOW_DANGGN_REWARD_NOTICE, false)

    init {
        getRankingData()
        checkLastRoundRewardPopup()
    }

    fun refreshRankingData() {
        mashUpScope {
            _isRefreshing.value = true
            getRankingData()
            delay(1000L)
            _isRefreshing.value = false
        }
    }

    /**
     * 단건 API를 호출해서, 랭킹 종료 초단위까지 받아와서 timer 돌림
     * 멈추고 나서 서버에서 어떻게 할 지 얘기해보기 다음 정보 없으면 ??:??:??으로 놔둠
     */
    private fun getTimerData(value: Int) = mashUpScope {
        danggnRepository.getDanggnSingleRound(value).also {
            if (it.isSuccess()) {
                suspendRunCatching {
                    timer.startTimer(
                        endTime = it.data?.endDate ?: throw ParseException("", 0)
                    ) { timer ->
                        timerCount.value = RankingItem.Timer(timerString = timer)
                    }
                }.getOrNull() ?: also {
                    timerCount.value = RankingItem.Timer(timerString = "??:??:??")
                }
            }
        }
    }

    internal fun getRankingData() = mashUpScope {
        updateAllRanking()
    }

    internal fun updateCurrentTabIndex(index: Int) = mashUpScope {
        currentTabIndex.emit(index)
    }

    /**
     * 밖에서도 호출할 수 있도록 private가 아닌 internal로 만들었습니다.
     */
    @SuppressLint("SimpleDateFormat")
    internal suspend fun updateAllRanking() {
        val allRoundListResponse = danggnRepository.getDanggnMultipleRound()
        if (allRoundListResponse.isSuccess()) {
            withContext(Dispatchers.Default) {
                val roundListData = allRoundListResponse.data?.danggnRankingRounds ?: listOf()
                val allRoundList = roundListData.mapIndexed { index, item ->
                    val (startDateString, endDateString, dateDiff) = try {
                        val roundFormat = SimpleDateFormat("yy.mm.dd")
                        val detailDateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")

                        val startDate = SimpleDateFormat("yyyy-mm-dd").parse(item.startDate)!!
                        val endDate = SimpleDateFormat("yyyy-mm-dd").parse(item.endDate)!!

                        val currentRoundDateDiff = if (index == 0) { // 현재 진행중인 랭킹일 때만 날짜 차이를 계산
                            val dateDiff = danggnRepository.getDanggnSingleRound(item.id).data.let {
                                val detailEndDate = it?.endDate?.let { date ->
                                    detailDateFormat.format(date)
                                }.orEmpty()
                                val endDateForDiff = detailDateFormat.parse(detailEndDate)
                                ((endDateForDiff.time - Calendar.getInstance().time.time) / DATE_UNIT).toInt()
                            }

                            if (dateDiff != -1) {
                                getTimerData(item.id)
                            }
                            dateDiff
                        } else {
                            null
                        }
                        (roundFormat.format(startDate) to roundFormat.format(endDate)) trip currentRoundDateDiff
                    } catch (c: CancellationException) {
                        throw c
                    } catch (ignore: Exception) {
                        ("????.??.??" to "????.??.??") trip null
                    }

                    AllRound(
                        id = item.id,
                        number = item.number,
                        startDate = startDateString,
                        endDate = endDateString,
                        dateDiff = dateDiff ?: -1,
                        isRunning = index == 0
                    )
                }.sortedByDescending { it.number }

                updateCurrentRoundId(allRoundList.first().id)
                _allDanggnRoundList.emit(allRoundList)
            }
        }
    }

    /**
     * 모든 멤버의 랭킹 리스트를 얻어옵니다
     */
    private suspend fun updatePersonalRankingList(rankingRoundId: Int) {
        val allMemberRankingResult = danggnRepository.getAllDanggnRank(
            danggnRankingRoundId = rankingRoundId,
            generationNumber = userPreferenceRepository.getCurrentGenerationNumber()
        )
        if (allMemberRankingResult.isSuccess()) {
            val rankingList = allMemberRankingResult.data ?: listOf()
            personalRankingList.emit(
                rankingList.map {
                    RankingItem.Ranking(
                        memberId = it.memberId.toString(),
                        text = it.memberName,
                        totalShakeScore = it.totalShakeScore
                    )
                }
            )
        }
    }

    /**
     * 플랫폼 랭킹을 얻어와 내 플랫폼 랭킹까지 업데이트 합니다.
     */
    private suspend fun updatePlatformRanking(rankingRoundId: Int) {
        val result = danggnRepository.getPlatformDanggnRank(
            danggnRankingRoundId = rankingRoundId,
            generationNumber = userPreferenceRepository.getCurrentGenerationNumber()
        )
        if (result.isSuccess()) {
            val updatedPlatformRankingList = (0..5).map { index ->
                result.data?.getOrNull(index)?.let {
                    RankingItem.PlatformRanking(
                        memberId = it.platform.detailName,
                        text = it.platform.detailName,
                        totalShakeScore = it.totalShakeScore
                    )
                } ?: RankingItem.EmptyRanking()
            }
            platformRankingList.emit(updatedPlatformRankingList)
        }
    }

    private fun getPlatformRankingItem(
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
    private fun getPersonalRankingItem(
        userPreference: UserPreference,
        personalRankingList: List<RankingItem>
    ): RankingItem {
        val myPersonalRank =
            personalRankingList.find { it.memberId == userPreference.id.toString() }
                ?: return RankingItem.EmptyRanking()
        val index = personalRankingList.indexOf(myPersonalRank)

        return RankingItem.MyRanking(
            memberId = myPersonalRank.memberId,
            totalShakeScore = myPersonalRank.totalShakeScore,
            text = "${index + 1}위"
        )
    }

    private suspend fun getFirstPlaceState(
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

    private fun checkLastRoundRewardPopup() = mashUpScope {
        kotlin.runCatching {
            popupRepository.getPopupKeyList().data
        }.onSuccess { popupList ->
            popupList?.find { DanggnPopupType.getDanggnPopupType(it) == DanggnPopupType.DANGGN_REWARD }
                ?.let {
                    val name = userPreferenceRepository.getUserPreference().first().name
                    val entity = getLastRoundRewardBottomPopupMessageFromStorage() ?: return@mashUpScope
                    _showLastRoundRewardPopup.emit(name to entity)
                }
        }
    }

    fun dismissLastRoundFirstPlacePopup() = mashUpScope {
        _showLastRoundRewardPopup.emit(null)
    }

    fun registerRewardNotice(roundId: Int, comment: String) {
        mashUpScope {
            kotlin.runCatching {
                danggnRepository.postDanggnRankingRewardComment(roundId, comment)
            }.onSuccess { result ->
                when {
                    result.isSuccess() && result.data == true -> {
                        updateSingleRound(currentRoundId.value)
                    }

                    result.isSuccess() && result.data != true -> {
                        handleErrorCode(BAD_REQUEST)
                    }

                    else -> handleErrorCode(result.code)
                }
            }.onFailure {
                handleErrorCode(BAD_REQUEST)
            }
        }
    }

    private suspend fun getLastRoundRewardBottomPopupMessageFromStorage(): MashUpPopupEntity? {
        return kotlin.runCatching {
            storageRepository.getStorage(DanggnPopupType.DANGGN_REWARD.name).data
        }.getOrNull()
            ?.let { result ->
                MashUpPopupEntity(
                    title = result.valueMap["title"] ?: "",
                    description = result.valueMap["subtitle"] ?: "",
                    imageResName = result.valueMap["imageName"] ?: "",
                    leftButtonText = result.valueMap["leftButtonTitle"] ?: "",
                    rightButtonText = result.valueMap["rightButtonTitle"] ?: ""
                )
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

    fun updateCurrentRoundId(roundId: Int) = mashUpScope {
        updateSingleRound(roundId)
        updatePersonalRankingList(roundId)
        updatePlatformRanking(roundId)

        _currentRoundId.emit(roundId)
    }

    fun confirmDanggnRewardNotice() {
        savedStateHandle[EXTRA_SHOW_DANGGN_REWARD_NOTICE] = false
    }

    /**
     * 모든 멤버의 랭킹 리스트를 얻어옵니다
     */
    private suspend fun updateSingleRound(rankingRoundId: Int) {
        val singleRoundResult = danggnRepository.getDanggnSingleRound(
            danggnRankingRoundId = rankingRoundId
        )
        if (singleRoundResult.isSuccess()) {
            _currentRound.emit(singleRoundResult.data)
        }
    }

    override fun onCleared() {
        super.onCleared()
        timer.stopTimer()
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
            override val totalShakeScore: Int = DEFAULT_SHAKE_NUMBER
        ) : RankingItem

        data class EmptyRanking(
            override val memberId: String = UUID.randomUUID().toString(),
            override val text: String = "",
            override val totalShakeScore: Int = DEFAULT_SHAKE_NUMBER
        ) : RankingItem

        /**
         * 플랫폼 랭킹 아이템
         */
        data class PlatformRanking(
            override val memberId: String = UUID.randomUUID().toString(),
            override val text: String = "",
            override val totalShakeScore: Int = DEFAULT_SHAKE_NUMBER
        ) : RankingItem

        data class MyRanking(
            override val memberId: String = "",
            override val totalShakeScore: Int = DEFAULT_SHAKE_NUMBER,
            override val text: String = ""
        ) : RankingItem

        data class MyPlatformRanking(
            override val text: String = "",
            override val memberId: String = UUID.randomUUID().toString(),
            override val totalShakeScore: Int = DEFAULT_SHAKE_NUMBER
        ) : RankingItem

        data class Timer(
            val timerString: String = ""
        )
    }

    sealed interface FirstRankingState {
        object Empty : FirstRankingState
        data class FirstRanking(val text: String) : FirstRankingState
    }

    data class AllRound(
        val id: Int = 0,
        val number: Int = 0,
        val startDate: String = "",
        val endDate: String = "",
        val dateDiff: Int = 0,
        val isRunning: Boolean = false
    )
}

data class RankingUiState(
    val firstPlaceState: DanggnRankingViewModel.FirstRankingState = DanggnRankingViewModel.FirstRankingState.Empty,
    val personalRankingList: List<DanggnRankingViewModel.RankingItem> = emptyList(),
    val platformRankingList: List<DanggnRankingViewModel.RankingItem> = emptyList(),
    val myPersonalRanking: DanggnRankingViewModel.RankingItem = DanggnRankingViewModel.RankingItem.EmptyRanking(),
    val myPlatformRanking: DanggnRankingViewModel.RankingItem = DanggnRankingViewModel.RankingItem.EmptyRanking(),
    val danggnAllRoundList: List<DanggnRankingViewModel.AllRound> = emptyList(),
    val timer: DanggnRankingViewModel.RankingItem.Timer = DanggnRankingViewModel.RankingItem.Timer()
)
