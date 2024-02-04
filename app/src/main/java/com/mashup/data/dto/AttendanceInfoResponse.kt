package com.mashup.data.dto

import com.mashup.core.model.AttendanceStatus
import com.mashup.data.model.AttendanceInfo
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.Date

@JsonClass(generateAdapter = true)
data class AttendanceInfoResponse(
    @field:Json(name = "attendanceInfos")
    val attendanceInfos: List<AttendanceInfo>,
    @field:Json(name = "memberName")
    val memberName: String
) {
    fun getAttendanceStatus(position: Int): AttendanceStatus {
        return attendanceInfos.getOrNull(position)?.status ?: AttendanceStatus.NOT_YET
    }

    fun getAttendanceAt(position: Int): Date? {
        return attendanceInfos.getOrNull(position)?.attendanceAt
    }

    fun getFinalAttendance(): AttendanceStatus {
        return when {
            this.attendanceInfos.any { info ->
                info.status == AttendanceStatus.ABSENT
            } -> {
                AttendanceStatus.ABSENT
            }
            this.attendanceInfos.any { info ->
                info.status == AttendanceStatus.LATE
            } -> {
                AttendanceStatus.LATE
            }
            this.attendanceInfos.any { info ->
                info.status == AttendanceStatus.ATTENDANCE
            } -> {
                AttendanceStatus.ATTENDANCE
            }
            else -> {
                AttendanceStatus.NOT_YET
            }
        }
    }
}
