package com.mashup.data.dto

import com.squareup.moshi.Json

class QrCreateRequest(
    @Json(name = "eventId")
    val eventId: Int,
    @Json(name = "code")
    val code: String,
    @Json(name = "start")
    val start: String,
    @Json(name = "end")
    val end: String,
)