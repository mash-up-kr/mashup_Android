package com.mashup.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class AttendanceCheckRequest(
    @field:Json(name = "eventId")
    val eventId: Int,
    @field:Json(name = "memberId")
    val memberId: Int
)