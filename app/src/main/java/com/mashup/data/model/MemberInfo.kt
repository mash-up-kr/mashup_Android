package com.mashup.data.model

import com.mashup.ui.attendance.model.AttendanceStatus
import com.squareup.moshi.Json

data class MemberInfo(
    @Json(name = "name")
    val name: String,
    @Json(name = "attendanceInfos")
    val attendanceInfos: List<AttendanceInfo>
) {
    val finalAttendance
        get() = when {
            attendanceInfos[0].status.uppercase() == "ATTEND"
                && attendanceInfos[1].status.uppercase() == "ATTEND" -> {
                AttendanceStatus.ATTEND
            }
            attendanceInfos[0].status.uppercase() == "ABSENCE"
                || attendanceInfos[1].status.uppercase() == "ABSENCE" -> {
                AttendanceStatus.ABSENCE
            }
            else -> {
                AttendanceStatus.LATENESS
            }
        }
}