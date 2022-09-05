package com.mashup.data.dto

import com.mashup.data.model.AttendanceInfo
import com.mashup.ui.attendance.model.AttendanceStatus
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.*

@JsonClass(generateAdapter = true)
data class AttendanceInfoResponse(
    @field:Json(name = "attendanceInfos")
    val attendanceInfos: List<AttendanceInfo>,
    @field:Json(name = "memberName")
    val memberName: String
) {
    fun getAttendance(position: Int): AttendanceStatus {
        return AttendanceStatus.getAttendanceStatus(attendanceInfos.getOrNull(position)?.status)
    }

    fun getAttendanceAt(position: Int): Date? {
        return attendanceInfos.getOrNull(position)?.attendanceAt
    }

    fun getFinalAttendance(): AttendanceStatus {
        return when {
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