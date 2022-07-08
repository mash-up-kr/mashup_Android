package com.mashup.data.dto

import com.squareup.moshi.Json

data class ScheduleResponse (
    @Json(name="scheduleId")
    val scheduleId: Int,
    @Json(name="generationNumber")
    val generationNum: Int,
    @Json(name="name")
    val name: String,
    @Json(name="eventList")
    val eventList: List<EventResponse>,
    @Json(name="startedAt")
    val startedAt: String,
    @Json(name="endedAt")
    val endedAt: String,
)
