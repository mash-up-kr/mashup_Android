package com.mashup.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class QrCreateRequest(
    @field:Json(name = "eventId")
    val eventId: Int,
    @field:Json(name = "code")
    val code: String,
    @field:Json(name = "start")
    val start: String,
    @field:Json(name = "end")
    val end: String
)
