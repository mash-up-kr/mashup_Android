package com.mashup.ui.schedule.util

import android.annotation.SuppressLint
import com.mashup.R
import com.mashup.core.model.AttendanceStatus
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@SuppressLint("SimpleDateFormat")
fun onBindAttendanceTime(
    time: Date?
): String {
    return if (time != null) {
        try {
            SimpleDateFormat("a hh:mm", Locale.KOREA).format(time)
        } catch (ignore: Exception) {
            "-"
        }
    } else {
        "-"
    }
}

fun onBindAttendanceImage(
    attendanceStatus: AttendanceStatus,
    isFinal: Boolean = false
): Int {
    return when (attendanceStatus) {
        AttendanceStatus.ABSENT -> {
            if (isFinal) {
                com.mashup.core.common.R.drawable.ic_absent_final
            } else {
                com.mashup.core.common.R.drawable.ic_absent_default
            }
        }
        AttendanceStatus.ATTENDANCE -> {
            if (isFinal) {
                com.mashup.core.common.R.drawable.ic_attendance_final
            } else {
                com.mashup.core.common.R.drawable.ic_attendance_default
            }
        }
        AttendanceStatus.LATE -> {
            if (isFinal) {
                com.mashup.core.common.R.drawable.ic_late_final
            } else {
                com.mashup.core.common.R.drawable.ic_late_default
            }
        }
        else -> {
            com.mashup.core.common.R.drawable.ic_attendance_not_yet
        }
    }
}

fun onBindAttendanceStatus(
    attendanceStatus: AttendanceStatus,
    isFinal: Boolean = false
): Int {
    return when (attendanceStatus) {
        AttendanceStatus.ABSENT -> {
            if (isFinal) { R.string.attendance_status_absent_final } else { R.string.attendance_status_absent }
        }
        AttendanceStatus.ATTENDANCE -> {
            if (isFinal) { R.string.attendance_status_attendance_final } else { R.string.attendance_status_attendance }
        }
        AttendanceStatus.LATE -> {
            if (isFinal) { R.string.attendance_status_late_final } else { R.string.attendance_status_late }
        }
        else -> { R.string.attendance_nothing }
    }
}
