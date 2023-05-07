package com.mashup.ui.schedule

import com.mashup.core.common.base.BaseViewModel
import com.mashup.data.dto.AttendanceInfoResponse
import com.mashup.data.dto.ScheduleListResponse
import com.mashup.data.dto.ScheduleResponse
import com.mashup.data.dto.SchedulesProgress
import com.mashup.data.repository.AttendanceRepository
import com.mashup.data.repository.ScheduleRepository
import com.mashup.datastore.data.repository.AppPreferenceRepository
import com.mashup.datastore.data.repository.UserPreferenceRepository
import com.mashup.ui.schedule.model.ScheduleCard
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val userPreferenceRepository: UserPreferenceRepository,
    private val appPreferenceRepository: AppPreferenceRepository,
    private val scheduleRepository: ScheduleRepository,
    private val attendanceRepository: AttendanceRepository
) : BaseViewModel() {
    private val _scheduleState = MutableStateFlow<ScheduleState>(ScheduleState.Empty)
    val scheduleState: StateFlow<ScheduleState> = _scheduleState

    private val _showCoachMark = MutableSharedFlow<Unit>()
    val showCoachMark: SharedFlow<Unit> = _showCoachMark

    fun getScheduleList() {
        mashUpScope {
            _scheduleState.emit(ScheduleState.Loading)
            val generateNumber =
                userPreferenceRepository.getUserPreference().first().generationNumbers.last()

            val response = scheduleRepository.getScheduleList(generateNumber)
            val data: ScheduleListResponse? = response.data

            if (!response.isSuccess() || data == null) {
                handleErrorCode(response.code)
                _scheduleState.emit(
                    ScheduleState.Success(
                        scheduleTitleState = ScheduleTitleState.Empty,
                        scheduleList = listOf(ScheduleCard.EmptySchedule()),
                        schedulePosition = 0
                    )
                )
                return@mashUpScope
            }
            _scheduleState.emit(
                ScheduleState.Success(
                    scheduleTitleState = when {
                        data.progress == SchedulesProgress.DONE -> {
                            ScheduleTitleState.End(generateNumber)
                        }
                        data.progress == SchedulesProgress.NOT_REGISTERED -> {
                            ScheduleTitleState.Empty
                        }
                        data.progress == SchedulesProgress.ON_GOING &&
                                data.dateCount != null -> {
                            ScheduleTitleState.DateCount(data.dateCount)
                        }
                        else -> {
                            ScheduleTitleState.SchedulePreparing
                        }
                    },
                    scheduleList = if (data.scheduleList.isEmpty()) {
                        listOf(ScheduleCard.EmptySchedule())
                    } else {
                        data.scheduleList.map { mapperToScheduleCard(it) }
                    },
                    schedulePosition = getSchedulePosition(data.scheduleList)
                )
            )
            showCoachMark(data.scheduleList)
        }
    }

    override fun handleErrorCode(code: String) {
        mashUpScope {
            _scheduleState.emit(ScheduleState.Error(code))
        }
    }

    private suspend fun mapperToScheduleCard(scheduleResponse: ScheduleResponse): ScheduleCard {
        if (scheduleResponse.eventList.isEmpty()) {
            return ScheduleCard.EmptySchedule(scheduleResponse)
        }

        val attendResponse = attendanceRepository.getScheduleAttendanceInfo(
            scheduleResponse.scheduleId
        )
        val data: AttendanceInfoResponse? = attendResponse.data

        return when {
            !attendResponse.isSuccess() || data == null ||
                    data.attendanceInfos.isEmpty() -> {
                ScheduleCard.InProgressSchedule(
                    scheduleResponse = scheduleResponse,
                    attendanceInfo = attendResponse.data
                )
            }
            else -> {
                ScheduleCard.EndSchedule(
                    scheduleResponse = scheduleResponse,
                    attendanceInfo = data
                )
            }
        }
    }

    private fun getSchedulePosition(schedules: List<ScheduleResponse>): Int {
        return schedules.size - schedules.filter { it.dateCount >= 0 }.size
    }

    private fun showCoachMark(schedules: List<ScheduleResponse>) {
        mashUpScope {
            val isShowCoachMark =
                appPreferenceRepository.getAppPreference().first().isShowCoachMarkInScheduleList
            val showCoachMark = schedules.any { it.dateCount >= 0 } && isShowCoachMark
            if (showCoachMark) {
                appPreferenceRepository.updateCoachMarkScheduleList(false)
                _showCoachMark.emit(Unit)
            }
        }
    }
}

sealed interface ScheduleTitleState {
    data class DateCount(val dataCount: Int) : ScheduleTitleState
    data class End(val generatedNumber: Int) : ScheduleTitleState
    object Empty : ScheduleTitleState
    object SchedulePreparing : ScheduleTitleState
}

sealed interface ScheduleState {
    object Empty : ScheduleState
    object Loading : ScheduleState
    data class Success(
        val scheduleTitleState: ScheduleTitleState,
        val scheduleList: List<ScheduleCard>,
        val schedulePosition: Int
    ) : ScheduleState

    data class Error(val code: String) : ScheduleState
}
