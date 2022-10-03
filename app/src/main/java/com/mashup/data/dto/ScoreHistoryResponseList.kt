package com.mashup.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ScoreHistoryResponseList(
    @field:Json(name = "scoreHistoryResponseList")
    val scoreHistoryResponseList: List<ScoreHistoryResponse>
)
