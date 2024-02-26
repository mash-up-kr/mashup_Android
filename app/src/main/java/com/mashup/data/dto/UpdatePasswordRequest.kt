package com.mashup.data.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UpdatePasswordRequest(
    val newPassword: String
)
