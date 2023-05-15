package com.mashup.feature.danggn.data.dto

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