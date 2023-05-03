package com.mashup.feature.danggn.data.dto

import com.mashup.core.model.Platform
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DanggnPlatformRankResponse(
    @Json(name = "platform")
    val platform: Platform,
    @Json(name = "totalShakeScore")
    val totalShakeScore: Int
)
