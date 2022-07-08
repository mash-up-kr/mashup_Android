package com.mashup.data.model

import com.squareup.moshi.Json

data class AttendanceInfo(
    @Json(name = "attendanceAt")
    val attendanceAt: String,
    @Json(name = "status")
    val status: String,
)
