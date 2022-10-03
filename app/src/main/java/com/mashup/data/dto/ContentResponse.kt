package com.mashup.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.*

@JsonClass(generateAdapter = true)
data class ContentResponse(
    @field:Json(name = "contentId")
    val contentId: Int,
    @field:Json(name = "title")
    val title: String,
    @field:Json(name = "content")
    val content: String,
    @field:Json(name = "startedAt")
    val startedAt: Date
)
