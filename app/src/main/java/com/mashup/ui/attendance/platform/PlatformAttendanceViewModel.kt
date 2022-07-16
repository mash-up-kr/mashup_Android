package com.mashup.ui.attendance.platform

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.mashup.base.BaseViewModel
import com.mashup.data.repository.AttendanceRepository
import com.mashup.ui.attendance.model.PlatformAttendance
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PlatformAttendanceViewModel @Inject constructor(
    private val attendanceRepository: AttendanceRepository
) : BaseViewModel() {
    private val _notice = mutableStateOf("")
    val notice: State<String> = _notice

    val platformList = mutableStateOf<List<PlatformAttendance>>(emptyList())

    init {
        getAttendanceNotice()
        getPlatformAttendanceList()
    }

    fun getPlatformAttendanceList() = mashUpScope {
        platformList.value = attendanceRepository.getPlatformList()
    }

    fun getAttendanceNotice() = mashUpScope {
        _notice.value = attendanceRepository.getNotice()
    }
}