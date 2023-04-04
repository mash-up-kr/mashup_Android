package com.mashup.ui.mypage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mashup.core.common.base.BaseViewModel
import com.mashup.data.dto.ScoreHistoryResponse
import com.mashup.data.repository.MyPageRepository
import com.mashup.datastore.data.repository.UserPreferenceRepository
import com.mashup.ui.model.ActivityHistory
import com.mashup.ui.model.AttendanceModel
import com.mashup.ui.model.Profile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val userPreferenceRepository: UserPreferenceRepository,
    private val myPageRepository: MyPageRepository
) : BaseViewModel() {

    private val _attendanceList = MutableLiveData<List<AttendanceModel>>()
    val attendanceList: LiveData<List<AttendanceModel>> = _attendanceList

    private val _errorCode = MutableSharedFlow<String>()
    val errorCode: SharedFlow<String> = _errorCode

    init {
        getMember()
    }

    fun getMember() = mashUpScope {
        val userPreference = userPreferenceRepository.getUserPreference().first()
        getScore(
            Profile(
                platform = userPreference.platform,
                name = userPreference.name,
                score = 0.0,
                generationNumber = userPreference.generationNumbers.last()
            )
        )
    }

    private fun getScore(profile: Profile) = mashUpScope {
        val response = myPageRepository.getScoreHistory()
        if (response.isSuccess()) {
            // 현재 기수만 사용(필터링)
            val filterItem = response.data?.scoreHistoryResponseList?.filter {
                it.generationNumber == profile.generationNumber
            }

            val attendanceItem =
                if (filterItem?.isNotEmpty() == true) {
                    attendanceScoreList(
                        profile.copy(score = filterItem.first().totalScore),
                        filterItem
                    )
                } else {
                    attendanceEmpty(profile)
                }
            _attendanceList.postValue(attendanceItem)
        } else {
            handleErrorCode(response.code)
        }
    }

    private fun attendanceScoreList(
        profile: Profile,
        filterItem: List<ScoreHistoryResponse>
    ): MutableList<AttendanceModel> {
        val attendanceItem = mutableListOf<AttendanceModel>()
        attendanceItem.addAll(myPageHeader(profile))

        filterItem.forEach {
            attendanceItem += AttendanceModel(
                id = attendanceItem.size,
                myPageType = MyPageAdapterType.LIST_LEVEL,
                generationNum = it.generationNumber
            )
            attendanceItem += it.scoreDetails.map { score ->
                AttendanceModel(
                    id = attendanceItem.size,
                    myPageType = MyPageAdapterType.LIST_ITEM,
                    generationNum = it.generationNumber,
                    activityHistory = ActivityHistory(
                        scoreName = score.scoreName,
                        attendanceType = AttendanceType.getAttendanceType(score.scoreType),
                        cumulativeScore = score.cumulativeScore,
                        score = score.score,
                        detail = score.scheduleName,
                        date = score.date
                    )
                )
            }
        }
        return attendanceItem
    }

    private fun myPageHeader(profile: Profile) = listOf(
        AttendanceModel(id = 0, MyPageAdapterType.TITLE, profile),
        AttendanceModel(id = 1, MyPageAdapterType.SCORE, profile)
    )

    private fun attendanceEmpty(profile: Profile): List<AttendanceModel> {
        val headers = myPageHeader(profile)
        return headers + AttendanceModel(headers.size, MyPageAdapterType.LIST_NONE)
    }

    override fun handleErrorCode(code: String) {
        mashUpScope {
            _errorCode.emit(code)
        }
    }
}
