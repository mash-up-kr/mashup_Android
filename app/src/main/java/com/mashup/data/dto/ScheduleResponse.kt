package com.mashup.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ScheduleResponse(
    @field:Json(name = "scheduleId")
    val scheduleId: Int,
    @field:Json(name = "generationNumber")
    val generationNum: Int,
    @field:Json(name = "name")
    val name: String,
    @field:Json(name = "eventList")
    val eventList: List<EventResponse>,
    @field:Json(name = "startedAt")
    val startedAt: String,
    @field:Json(name = "endedAt")
    val endedAt: String,
)
