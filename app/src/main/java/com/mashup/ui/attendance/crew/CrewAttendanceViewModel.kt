package com.mashup.ui.attendance.crew

import androidx.lifecycle.SavedStateHandle
import com.mashup.core.common.base.BaseViewModel
import com.mashup.core.common.constant.BAD_REQUEST
import com.mashup.data.dto.PlatformAttendanceResponse
import com.mashup.data.model.PlatformInfo
import com.mashup.data.repository.AttendanceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class CrewAttendanceViewModel @Inject constructor(
    private val attendanceRepository: AttendanceRepository,
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    private val platformAttendance = savedStateHandle.get<PlatformInfo>(EXTRA_PLATFORM_KEY)

    private val scheduleId
        get() = savedStateHandle.get<Int>(EXTRA_SCHEDULE_ID)

    private val _crewAttendanceState =
        MutableStateFlow<CrewAttendanceState>(CrewAttendanceState.Empty)
    val crewAttendanceState: StateFlow<CrewAttendanceState> =
        _crewAttendanceState

    init {
        getCrewAttendanceList()
    }

    private fun getCrewAttendanceList() = mashUpScope {
        _crewAttendanceState.emit(CrewAttendanceState.Loading)
        val platformName = platformAttendance?.platform?.name?.uppercase()
        val scheduleId = scheduleId
        if (platformName == null || scheduleId == null) {
            handleErrorCode(BAD_REQUEST)
            return@mashUpScope
        }
        attendanceRepository.getCrewAttendanceList(
            platformName = platformName,
            scheduleId = scheduleId
        ).onSuccess { response ->
            if (platformAttendance != null) {
                _crewAttendanceState.emit(
                    CrewAttendanceState.Success(
                        title = "${platformAttendance.platform.detailName}(${platformAttendance.totalCount}명)",
                        crewAttendance = response
                    )
                )
            }
        }.onFailure { code ->
            handleErrorCode(code)
        }
    }

    companion object {
        const val EXTRA_PLATFORM_KEY = "EXTRA_PLATFORM_KEY"
        const val EXTRA_SCHEDULE_ID = "EXTRA_SCHEDULE_ID"
    }

    override fun handleErrorCode(code: String) {
        mashUpScope {
            _crewAttendanceState.emit(CrewAttendanceState.Error(code))
        }
    }
}

sealed interface CrewAttendanceState {
    object Empty : CrewAttendanceState
    object Loading : CrewAttendanceState
    data class Success(
        val title: String,
        val crewAttendance: PlatformAttendanceResponse
    ) : CrewAttendanceState

    data class Error(val code: String) : CrewAttendanceState
}
