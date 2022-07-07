package com.mashup.ui.attendance.model

data class CrewAttendance(
    val name: String,
    val firstSeminarAttendance: AttendanceStatus,
    val firstSeminarAttendanceTimeStamp: String,
    val secondSeminarAttendance: AttendanceStatus,
    val secondSeminarAttendanceTimeStamp: String
) {

    val finalAttendance
        get() = when {
            firstSeminarAttendance == AttendanceStatus.ATTEND
                && secondSeminarAttendance == AttendanceStatus.ATTEND -> {
                AttendanceStatus.ATTEND
            }
            firstSeminarAttendance == AttendanceStatus.ABSENCE
                || secondSeminarAttendance == AttendanceStatus.ABSENCE -> {
                AttendanceStatus.ABSENCE
            }
            else -> {
                AttendanceStatus.LATENESS
            }
        }
}
