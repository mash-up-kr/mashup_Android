package com.mashup.feature.danggn.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class DanggnRandomTodayMessageResponse(
    @field:Json(name = "todayMessage")
    val todayMessage: String,
)
