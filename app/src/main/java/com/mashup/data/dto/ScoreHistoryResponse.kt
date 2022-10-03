package com.mashup.data.dto

import com.mashup.data.model.ScoreDetails
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ScoreHistoryResponse(
    @field:Json(name = "generationNumber")
    val generationNumber: Int,
    @field:Json(name = "scoreDetails")
    val scoreDetails: List<ScoreDetails>,
    @field:Json(name = "totalScore")
    val totalScore: Double
)
