package com.mashup.core.model

enum class AttendanceStatus {
    ATTENDANCE,
    ABSENT,
    LATE,
    NOT_YET;

    companion object {
        fun getAttendanceStatus(value: String?) =
            values().find { it.name.uppercase() == value?.uppercase() } ?: NOT_YET
    }
}
