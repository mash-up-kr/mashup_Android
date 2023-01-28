package com.mashup.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class AccessResponse(
    @field:Json(name = "generations")
    val generationNumbers: List<Int>,
    @field:Json(name = "memberId")
    val memberId: Int,
    @field:Json(name = "name")
    val name: String,
    @field:Json(name = "platform")
    val platform: String,
    @field:Json(name = "token")
    val token: String,
    @field:Json(name = "pushNotificationAgreed")
    val pushNotificationAgreed: Boolean
)
