package com.mashup.data.dto

import com.mashup.data.model.Platform
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SignUpRequest(
    @field:Json(name = "identification")
    val identification: String,
    @field:Json(name = "inviteCode")
    val inviteCode: String,
    @field:Json(name = "name")
    val name: String,
    @field:Json(name = "password")
    val password: String,
    @field:Json(name = "platform")
    val platform: Platform,
    @field:Json(name = "privatePolicyAgreed")
    val privatePolicyAgreed: Boolean,
)