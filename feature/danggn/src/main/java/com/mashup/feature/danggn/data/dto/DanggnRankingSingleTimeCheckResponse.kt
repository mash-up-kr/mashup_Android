package com.mashup.feature.danggn.data.dto
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DanggnRankingSingleTimeCheckResponse(
    @Json(name = "danggnRankingReward")
    val danggnRankingReward: DanggnRankingReward,
    @Json(name = "dateCount")
    val dateCount: Int,
    @Json(name = "endDate")
    val endDate: String,
    @Json(name = "number")
    val number: Int,
    @Json(name = "startDate")
    val startDate: String
) {
    @JsonClass(generateAdapter = true)
    data class DanggnRankingReward(
        @Json(name = "comment")
        val comment: Any,
        @Json(name = "id")
        val id: Any,
        @Json(name = "isFirstPlaceMember")
        val isFirstPlaceMember: Boolean,
        @Json(name = "name")
        val name: Any,
        @Json(name = "status")
        val status: String
    )
}