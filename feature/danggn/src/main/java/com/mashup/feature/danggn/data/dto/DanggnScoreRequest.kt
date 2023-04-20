package com.mashup.feature.danggn.data.dto

import com.squareup.moshi.Json

data class DanggnScoreRequest(
    @Json(name = "score") val score: Int
)
