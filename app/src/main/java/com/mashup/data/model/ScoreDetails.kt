package com.mashup.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.*

@JsonClass(generateAdapter = true)
data class ScoreDetails(
    @Json(name = "cumulativeScore")
    val cumulativeScore: Double,
    @Json(name = "date")
    val date: Date,
    @Json(name = "scheduleName")
    val scheduleName: String,
    @Json(name = "scoreType")
    val scoreType: String,
    @Json(name = "isCancelled")
    val isCancelled: Boolean,
    @Json(name = "scoreName")
    val scoreName: String,
    @Json(name = "score")
    val score: Double
)
