package com.mashup.feature.danggn.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DanggnRankingMultipleRoundCheckResponse(
    @Json(name = "danggnRankingRounds")
    val danggnRankingRounds: List<DanggnRankingRound>
) {
    @JsonClass(generateAdapter = true)
    data class DanggnRankingRound(
        @Json(name = "endDate")
        val endDate: String,
        @Json(name = "id")
        val id: Int,
        @Json(name = "number")
        val number: Int,
        @Json(name = "startDate")
        val startDate: String
    )
}