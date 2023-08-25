package com.mashup.feature.mypage.profile.data.dto


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MemberGenerationRequest(
    @Json(name = "projectTeamName")
    val projectTeamName: String,
    @Json(name = "role")
    val role: String
)