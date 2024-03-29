package com.mashup.ui.attendance.platform

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import com.mashup.constant.EXTRA_SCHEDULE_ID
import com.mashup.core.common.base.BaseViewModel
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
        attendanceRepository.getPlatformAttendanceList(scheduleId)
            .onSuccess { response ->
                val notice = when {
                    response.eventNum == 0 -> {
                        "아직 일정 시작 전이예요."
                    }
                    (response.eventNum == 1 || response.eventNum == 2) && !response.isEnd -> {
                        "출석체크가 실시간으로 진행되고 있어요"
                    }
                    response.eventNum == 1 -> {
                        "1부 출석이 완료되었어요."
                    }
                    response.eventNum == 2 -> {
                        "출석체크가 완료되었어요"
                    }
                    else -> {
                        "서버에서 이상한 일이 발생했어요 ㅜ"
                    }
                }

                _platformAttendanceState.value =
                    PlatformAttendanceState.Success(
                        notice = notice,
                        totalAttendance = response
                    )
            }
            .onFailure { code ->
                handleErrorCode(code)
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
