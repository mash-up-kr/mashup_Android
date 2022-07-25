package com.mashup.data.model

import com.squareup.moshi.Json
import java.util.*

data class AttendanceInfo(
    @Json(name = "attendanceAt")
    val attendanceAt: Date,
    @Json(name = "status")
    val status: String,
)
