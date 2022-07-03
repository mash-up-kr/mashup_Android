package com.mashup.data.repository

import com.mashup.ui.attendance.model.PlatformAttendance
import com.mashup.ui.model.Platform
import javax.inject.Inject

class AttendanceRepository @Inject constructor() {
    suspend fun getNotice(): String {
        return "공지사항입니다."
    }

    suspend fun getPlatformList(): List<PlatformAttendance> {
        return listOf(
            PlatformAttendance(
                platform = Platform.Design,
                currentAttendanceCrew = 7,
                maxAttendanceCrew = 10
            ),
            PlatformAttendance(
                platform = Platform.Android,
                currentAttendanceCrew = 3,
                maxAttendanceCrew = 18
            ),
            PlatformAttendance(
                platform = Platform.Web,
                currentAttendanceCrew = 10,
                maxAttendanceCrew = 20
            ),
            PlatformAttendance(
                platform = Platform.iOS,
                currentAttendanceCrew = 9,
                maxAttendanceCrew = 10
            )
        )
    }
}