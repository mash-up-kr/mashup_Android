package com.mashup.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ScheduleCreateRequest(
    @field:Json(name = "generationId")
    val generationId: Int,
    @field:Json(name = "name")
    val name: String,
    @field:Json(name = "startedAt")
    val startedAt: String,
    @field:Json(name = "endedAt")
    val endedAt: String
)
