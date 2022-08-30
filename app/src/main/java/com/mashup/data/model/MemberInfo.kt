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
            attendanceInfos.isEmpty() -> {
                AttendanceStatus.NONE
            }
            attendanceInfos[0].status.uppercase() == "ATTENDANCE"
                && attendanceInfos[1].status.uppercase() == "ATTENDANCE" -> {
                AttendanceStatus.ATTENDANCE
            }
            attendanceInfos[0].status.uppercase() == "ABSENT"
                || attendanceInfos[1].status.uppercase() == "ABSENT" -> {
                AttendanceStatus.ABSENT
            }
            else -> {
                AttendanceStatus.LATE
            }
        }
    }
}