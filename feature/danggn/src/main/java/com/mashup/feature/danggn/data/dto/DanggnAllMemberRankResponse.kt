package com.mashup.feature.danggn.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DanggnAllMemberRankResponse(
    @Json(name = "danggnMemberRankDataList")
    val allMemberRankList: List<DanggnMemberRankResponse>,
    @Json(name = "limit")
    val limit: Int,
)