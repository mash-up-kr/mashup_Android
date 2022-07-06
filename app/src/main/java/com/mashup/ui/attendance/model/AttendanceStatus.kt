package com.mashup.ui.attendance.model

import com.mashup.R

enum class AttendanceStatus(
    val label: String,
    val iconRes: Int
) {
    ATTEND(label = "출석", iconRes = R.drawable.ic_check),
    ABSENCE(label = "결석", iconRes = R.drawable.ic_xmark),
    LATENESS(label = "지각", iconRes = R.drawable.ic_triangle),
    NONE(label = "-", iconRes = R.drawable.ic_circle)
}