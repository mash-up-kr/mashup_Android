package com.mashup.ui.attendance.crew

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import com.mashup.base.BaseViewModel
import com.mashup.data.repository.AttendanceRepository
import com.mashup.ui.attendance.model.CrewAttendance
import com.mashup.ui.attendance.model.PlatformAttendance
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CrewAttendanceViewModel @Inject constructor(
    private val attendanceRepository: AttendanceRepository,
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    val platformAttendance
        get() = savedStateHandle.getLiveData<PlatformAttendance>(EXTRA_PLATFORM_KEY)

    val crewList = mutableStateOf<List<CrewAttendance>>(emptyList())

    init {
        getCrewAttendanceList()
    }

    fun getCrewAttendanceList() = mashUpScope {
        crewList.value = attendanceRepository.getCrewAttendanceList()
    }

    companion object {
        const val EXTRA_PLATFORM_KEY = "EXTRA_PLATFORM_KEY"
    }
}