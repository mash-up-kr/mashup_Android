package com.mashup.data.dto

import com.squareup.moshi.Json

data class EventResponse (
    @Json(name="eventId")
    val eventId: Int,
    @Json(name="attendanceCode")
    val attendanceCode: AttendanceCodeResponse,
    @Json(name="contentList")
    val contentList: List<ContentResponse>,
    @Json(name="startedAt")
    val startedAt: String,
    @Json(name="endedAt")
    val endedAt: String,
)