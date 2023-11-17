package com.mashup.feature.mypage.profile.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MemberGenerationsResponse(
    @Json(name = "memberGenerations")
    val memberGenerations: List<MemberGeneration>
) {
    @JsonClass(generateAdapter = true)
    data class MemberGeneration(
        @Json(name = "id")
        val id: Int,
        @Json(name = "number")
        val number: Int,
        @Json(name = "platform")
        val platform: String,
        @Json(name = "projectTeamName")
        val projectTeamName: String?,
        @Json(name = "role")
        val role: String?,
        @Json(name = "status")
        val status: String,
    )
}
