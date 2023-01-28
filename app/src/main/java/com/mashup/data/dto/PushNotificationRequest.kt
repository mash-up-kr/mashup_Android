package com.mashup.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class PushNotificationRequest(
    @field:Json(name = "pushNotificationAgreed")
    val pushNotificationAgreed: Boolean
)
