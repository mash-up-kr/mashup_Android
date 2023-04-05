package com.mashup.data.dto
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DanggnMemberRankResponse(
    @Json(name = "code")
    val code: String,
    @Json(name = "data")
    val `data`: List<Data>,
    @Json(name = "message")
    val message: String,
    @Json(name = "page")
    val page: Page
) {
    data class Data(
        @Json(name = "memberId")
        val memberId: Int,
        @Json(name = "memberName")
        val memberName: String,
        @Json(name = "totalShakeScore")
        val totalShakeScore: Int
    )

    data class Page(
        @Json(name = "number")
        val number: Int,
        @Json(name = "size")
        val size: Int,
        @Json(name = "totalCount")
        val totalCount: Int
    )
}
