package com.mashup.data.dto

import com.mashup.data.model.Platform
import com.squareup.moshi.Json

data class SignUpRequest(
    @Json(name = "identification")
    val identification: String,
    @Json(name = "inviteCode")
    val inviteCode: String,
    @Json(name = "name")
    val name: String,
    @Json(name = "password")
    val password: String,
    @Json(name = "platform")
    val platform: Platform,
    @Json(name = "privatePolicyAgreed")
    val privatePolicyAgreed: Boolean,
)