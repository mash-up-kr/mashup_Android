package com.mashup.ui.attendance.model

import com.mashup.R

enum class AttendanceStatus(
    val label: String,
    val iconRes: Int
) {
    ATTENDANCE(label = "출석", iconRes = R.drawable.ic_check),
    ABSENT(label = "결석", iconRes = R.drawable.ic_xmark),
    LATE(label = "지각", iconRes = R.drawable.ic_triangle),
    NONE(label = "-", iconRes = R.drawable.ic_circle)
}