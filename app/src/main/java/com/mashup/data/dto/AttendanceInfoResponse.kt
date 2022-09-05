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
    fun getAttendanceStatus(position: Int): AttendanceStatus {
        return AttendanceStatus.getAttendanceStatus(attendanceInfos.getOrNull(position)?.status)
    }

    fun getAttendanceAt(position: Int): Date? {
        return attendanceInfos.getOrNull(position)?.attendanceAt
    }

    fun getFinalAttendance(): AttendanceStatus {
        return when {
            getAttendanceStatus(0) == AttendanceStatus.ATTENDANCE
                && getAttendanceStatus(1) == AttendanceStatus.ATTENDANCE -> {
                AttendanceStatus.ATTENDANCE
            }
            getAttendanceStatus(0) == AttendanceStatus.NOT_YET
                || getAttendanceStatus(1) == AttendanceStatus.NOT_YET -> {
                AttendanceStatus.NOT_YET
            }
            getAttendanceStatus(0) == AttendanceStatus.ABSENT
                || getAttendanceStatus(1) == AttendanceStatus.ABSENT -> {
                AttendanceStatus.ABSENT
            }
            else -> {
                AttendanceStatus.LATE
            }
        }
    }
}