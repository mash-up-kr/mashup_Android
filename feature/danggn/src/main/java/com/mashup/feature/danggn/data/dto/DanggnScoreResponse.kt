package com.mashup.feature.danggn.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DanggnScoreResponse(
    @field:Json(name = "totalShakeScore")
    val totalShakeScore: Int
)
