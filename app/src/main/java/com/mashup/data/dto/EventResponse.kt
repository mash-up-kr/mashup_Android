package com.mashup.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.*

@JsonClass(generateAdapter = true)
data class EventResponse(
    @field:Json(name = "eventId")
    val eventId: Int,
    @field:Json(name = "attendanceCode")
    val attendanceCode: AttendanceCodeResponse,
    @field:Json(name = "contentList")
    val contentList: List<ContentResponse>,
    @field:Json(name = "startedAt")
    val startedAt: Date,
    @field:Json(name = "endedAt")
    val endedAt: Date,
)