package com.mashup.ui.attendance.model

import com.mashup.ui.model.Platform

data class PlatformAttendance(
    val platform: Platform,
    val numberOfAttend: Int,
    val numberOfLateness: Int,
    val numberOfAbsence: Int
)
