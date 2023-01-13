package com.mashup.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginRequest(
    @field:Json(name = "identification")
    val identification: String,
    @field:Json(name = "password")
    val password: String,
    @field:Json(name = "fcmToken")
    val fcmToken: String,
    @field:Json(name = "osType")
    val osType: String = "ANDROID"
)
