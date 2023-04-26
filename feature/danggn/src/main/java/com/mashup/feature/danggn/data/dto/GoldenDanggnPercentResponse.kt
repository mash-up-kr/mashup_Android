package com.mashup.feature.danggn.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GoldenDanggnPercentResponse(
    @Json(name = "goldenDanggnPercent") val goldenDanggnPercent: Int?
)
