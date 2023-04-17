package com.mashup.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DanggnMemberRankResponse(
    @Json(name = "memberId")
    val memberId: Int,
    @Json(name = "memberName")
    val memberName: String,
    @Json(name = "totalShakeScore")
    val totalShakeScore: Int
)

@JsonClass(generateAdapter = true)
data class DanggnAllMemberRankResponse(
    @Json(name = "danggnMemberRankDataList")
    val allMemberRankList: List<DanggnMemberRankResponse>,
    @Json(name = "limit")
    val limit: Int,
)