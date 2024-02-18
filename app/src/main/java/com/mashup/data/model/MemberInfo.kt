package com.mashup.data.model

import com.mashup.core.model.AttendanceStatus
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * AttendanceInfoResponse랑 합치면 좋을 듯
 */
@JsonClass(generateAdapter = true)
data class MemberInfo(
    @field:Json(name = "name")
    val name: String,
    @field:Json(name = "attendanceInfos")
    val attendanceInfos: List<AttendanceInfo>
) {
    private fun getAttendanceStatus(position: Int): AttendanceStatus {
        return attendanceInfos.getOrNull(position)?.status ?: AttendanceStatus.NOT_YET
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
            else -> {
                if (this.attendanceInfos.any { info ->
                        info.status == AttendanceStatus.NOT_YET
                    }
                ) {
                    AttendanceStatus.NOT_YET
                } else {
                    AttendanceStatus.ATTENDANCE
                }
            }
        }
    }
}
