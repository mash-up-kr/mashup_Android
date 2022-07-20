package com.mashup.ui.attendance.crew

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import com.mashup.base.BaseViewModel
import com.mashup.data.dto.PlatformAttendanceResponse
import com.mashup.data.dto.TotalAttendanceResponse
import com.mashup.data.model.PlatformInfo
import com.mashup.data.repository.AttendanceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CrewAttendanceViewModel @Inject constructor(
    private val attendanceRepository: AttendanceRepository,
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    val platformAttendance
        get() = savedStateHandle.getLiveData<PlatformInfo>(EXTRA_PLATFORM_KEY)

    val scheduleId
        get() = savedStateHandle.get<Int>(EXTRA_SCHEDULE_ID)

    private val _crewAttendanceState = mutableStateOf<CrewAttendanceState>(
        CrewAttendanceState.Empty
    )
    val crewAttendanceState: State<CrewAttendanceState> =
        _crewAttendanceState

    fun getCrewAttendanceList() = mashUpScope {
        val platformName = platformAttendance.value?.platform?.name?.uppercase()
        val scheduleId = scheduleId
        if (platformName == null || scheduleId == null) {
            return@mashUpScope
        }
        val response = attendanceRepository.getCrewAttendanceList(
            platformName = platformName,
            scheduleId = scheduleId
        )

        if (!response.isSuccess()) {
            _crewAttendanceState.value =
                CrewAttendanceState.Error(code = response.code, message = response.message)
            return@mashUpScope
        }

        response.data?.run {
            _crewAttendanceState.value =
                CrewAttendanceState.Success(response.data)
        }
    }

    companion object {
        const val EXTRA_PLATFORM_KEY = "EXTRA_PLATFORM_KEY"
        const val EXTRA_SCHEDULE_ID = "EXTRA_SCHEDULE_ID"
    }
}

sealed interface CrewAttendanceState {
    object Empty : CrewAttendanceState
    data class Success(val data: PlatformAttendanceResponse) : CrewAttendanceState
    data class Error(val code: String, val message: String?) : CrewAttendanceState
}