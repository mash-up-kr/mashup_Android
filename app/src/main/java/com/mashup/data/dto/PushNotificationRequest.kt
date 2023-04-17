package com.mashup.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class PushNotificationRequest(
    @field:Json(name = "newsPushNotificationAgreed")
    val pushNotificationAgreed: Boolean,
    @field:Json(name = "danggnPushNotificationAgreed")
    val danggnPushNotificationAgreed: Boolean
)
