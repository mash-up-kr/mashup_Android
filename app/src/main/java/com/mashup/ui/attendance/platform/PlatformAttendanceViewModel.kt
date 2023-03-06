package com.mashup.ui.attendance.platform

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import com.mashup.base.BaseViewModel
import com.mashup.constant.EXTRA_SCHEDULE_ID
import com.mashup.data.dto.TotalAttendanceResponse
import com.mashup.data.repository.AttendanceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PlatformAttendanceViewModel @Inject constructor(
    private val attendanceRepository: AttendanceRepository,
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    val scheduleId
        get() = savedStateHandle.get<Int>(EXTRA_SCHEDULE_ID)

    private val _platformAttendanceState = mutableStateOf<PlatformAttendanceState>(
        PlatformAttendanceState.Empty
    )
    val platformAttendanceState: State<PlatformAttendanceState> =
        _platformAttendanceState

    fun getPlatformAttendanceList() = mashUpScope {
        _platformAttendanceState.value = PlatformAttendanceState.Loading
        val scheduleId = scheduleId ?: return@mashUpScope
        val response = attendanceRepository.getPlatformAttendanceList(scheduleId)

        if (!response.isSuccess()) {
            handleErrorCode(response.code)
            return@mashUpScope
        }

        response.data?.run {
            val notice = when {
                eventNum == 0 -> {
                    "아직 일정 시작 전이예요."
                }
                (eventNum == 1 || eventNum == 2) && !isEnd -> {
                    "출석체크가 실시간으로 진행되고 있어요"
                }
                eventNum == 1 && isEnd -> {
                    "1부 출석이 완료되었어요."
                }
                eventNum == 2 && isEnd -> {
                    "출석체크가 완료되었어요"
                }
                else -> {
                    "서버에서 이상한 일이 발생했어요 ㅜ"
                }
            }

            _platformAttendanceState.value =
                PlatformAttendanceState.Success(
                    notice = notice,
                    totalAttendance = response.data
                )
        }
    }

    override fun handleErrorCode(code: String) {
        mashUpScope {
            _platformAttendanceState.value = PlatformAttendanceState.Error(code)
        }
    }
}

sealed interface PlatformAttendanceState {
    object Empty : PlatformAttendanceState
    object Loading : PlatformAttendanceState
    data class Success(
        val notice: String,
        val totalAttendance: TotalAttendanceResponse
    ) : PlatformAttendanceState

    data class Error(val code: String) : PlatformAttendanceState
}
