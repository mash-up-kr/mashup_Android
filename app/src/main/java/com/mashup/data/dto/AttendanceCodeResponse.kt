package com.mashup.data.dto

import com.squareup.moshi.Json

data class AttendanceCodeResponse (
    @Json(name="id")
    val id: Int,
    @Json(name="eventId")
    val eventId: Int,
    @Json(name="code")
    val code: String,
    @Json(name="startedAt")
    val startedAt: String,
    @Json(name="endedAt")
    val endedAt: String,
)