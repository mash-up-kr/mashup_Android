package com.mashup.data.dto

import com.squareup.moshi.Json

data class ScheduleCreateRequest(
    @Json(name = "generationId")
    val generationId: Int,
    @Json(name = "name")
    val name: String,
    @Json(name = "startedAt")
    val startedAt: String,
    @Json(name = "endedAt")
    val endedAt: String,
)