package com.mashup.data.dto

import com.squareup.moshi.Json

data class ContentResponse(
    @Json(name = "contentId")
    val contentId: Int,
    @Json(name = "title")
    val title: String,
    @Json(name = "content")
    val content: String,
    @Json(name = "startedAt")
    val startedAt: String,
)