package com.mashup.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class AttendanceCheckRequest(
    @field:Json(name = "checkingCode")
    val checkingCode: String
)
