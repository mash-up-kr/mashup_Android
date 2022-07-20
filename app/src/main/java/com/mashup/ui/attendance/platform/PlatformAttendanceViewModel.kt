package com.mashup.ui.attendance.platform

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import com.mashup.base.BaseViewModel
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

    private val _notice = mutableStateOf("")
    val notice: State<String> = _notice

    private val _platformAttendanceState = mutableStateOf<PlatformAttendanceState>(
        PlatformAttendanceState.Empty
    )
    val platformAttendanceState: State<PlatformAttendanceState> =
        _platformAttendanceState

    fun getPlatformAttendanceList() = mashUpScope {
        val scheduleId = scheduleId ?: return@mashUpScope
        val response = attendanceRepository.getPlatformAttendanceList(scheduleId)

        if (!response.isSuccess()) {
            _platformAttendanceState.value =
                PlatformAttendanceState.Error(response.code, response.message)
            return@mashUpScope
        }

        response.data?.run {
            _platformAttendanceState.value =
                PlatformAttendanceState.Success(response.data)
        }
    }

    companion object {
        const val EXTRA_SCHEDULE_ID = "EXTRA_SCHEDULE_ID"
    }
}

sealed interface PlatformAttendanceState {
    object Empty : PlatformAttendanceState
    data class Success(val data: TotalAttendanceResponse) : PlatformAttendanceState
    data class Error(val code: String, val message: String?) : PlatformAttendanceState
}