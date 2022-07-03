package com.mashup.ui.attendance.model

import com.mashup.ui.model.Platform

data class PlatformAttendance(
    val platform: Platform,
    val currentAttendanceCrew: Int,
    val maxAttendanceCrew: Int
)
