package com.mashup.data.model

import com.mashup.ui.attendance.model.AttendanceStatus
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MemberInfo(
    @field:Json(name = "name")
    val name: String,
    @field:Json(name = "attendanceInfos")
    val attendanceInfos: List<AttendanceInfo>
) {
    fun getFinalAttendance(): AttendanceStatus {
        return when {
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
}