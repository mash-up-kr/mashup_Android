package com.mashup.data.dto

import com.squareup.moshi.Json

class AttendanceCheckResponse(
    @Json(name = "status")
    val status: String,
)