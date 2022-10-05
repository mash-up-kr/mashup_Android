package com.mashup.data.model

import com.mashup.core.model.AttendanceStatus
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.*

@JsonClass(generateAdapter = true)
data class AttendanceInfo(
    @field:Json(name = "attendanceAt")
    val attendanceAt: Date?,
    @field:Json(name = "status")
    val status: AttendanceStatus
)
