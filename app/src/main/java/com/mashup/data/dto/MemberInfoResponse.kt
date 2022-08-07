package com.mashup.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MemberInfoResponse(
    @field:Json(name = "generationNumber")
    val generationNumber: Int,
    @field:Json(name = "id")
    val id: Int,
    @field:Json(name = "identification")
    val identification: String,
    @field:Json(name = "name")
    val name: String,
    @field:Json(name = "platform")
    val platform: String
)