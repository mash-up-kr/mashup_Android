package com.mashup.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.*

@JsonClass(generateAdapter = true)
data class AttendanceCodeResponse(
    @field:Json(name = "id")
    val id: Int,
    @field:Json(name = "eventId")
    val eventId: Int,
    @field:Json(name = "code")
    val code: String,
    @field:Json(name = "startedAt")
    val startedAt: Date,
    @field:Json(name = "endedAt")
    val endedAt: Date
)
