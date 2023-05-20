package com.mashup.feature.danggn.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DanggnMemberRankResponse(
    @field:Json(name = "memberId")
    val memberId: Int,
    @field:Json(name = "memberName")
    val memberName: String,
    @field:Json(name = "totalShakeScore")
    val totalShakeScore: Int
)