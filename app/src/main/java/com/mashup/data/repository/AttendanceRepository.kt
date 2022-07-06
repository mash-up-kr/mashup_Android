package com.mashup.data.repository

import com.mashup.ui.attendance.model.CrewAttendance
import com.mashup.ui.attendance.model.PlatformAttendance
import com.mashup.ui.model.Platform
import javax.inject.Inject

class AttendanceRepository @Inject constructor() {
    suspend fun getNotice(): String {
        return "공지사항입니다."
    }

    suspend fun getCrewAttendanceList(): List<CrewAttendance> {
        return emptyList()
    }

    suspend fun getPlatformList(): List<PlatformAttendance> {
        return listOf(
            PlatformAttendance(
                platform = Platform.Design,
                numberOfAttend = 7,
                numberOfAbsence = 10,
                numberOfLateness = 0
            ),
            PlatformAttendance(
                platform = Platform.Android,
                numberOfAttend = 3,
                numberOfAbsence = 18,
                numberOfLateness = 0
            ),
            PlatformAttendance(
                platform = Platform.Web,
                numberOfAttend = 10,
                numberOfAbsence = 20,
                numberOfLateness = 0
            ),
            PlatformAttendance(
                platform = Platform.iOS,
                numberOfAttend = 9,
                numberOfAbsence = 10,
                numberOfLateness = 0
            )
        )
    }
}