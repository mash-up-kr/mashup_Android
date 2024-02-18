package com.mashup.feature.mypage.profile.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MemberProfileResponse(
    @Json(name = "birthDate")
    val birthDate: String?,
    @Json(name = "blogLink")
    val blogLink: String?,
    @Json(name = "company")
    val company: String?,
    @Json(name = "githubLink")
    val githubLink: String?,
    @Json(name = "introduction")
    val introduction: String?,
    @Json(name = "job")
    val job: String?,
    @Json(name = "linkedInLink")
    val linkedInLink: String?,
    @Json(name = "memberId")
    val memberId: Int,
    @Json(name = "portfolioLink")
    val portfolioLink: String?,
    @Json(name = "residence")
    val residence: String?,
    @Json(name = "socialNetworkServiceLink")
    val socialNetworkServiceLink: String?,
    @Json(name = "memberGenerations")
    val memberGenerations: List<MemberGenerationsResponse.MemberGeneration>
)
