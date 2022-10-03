package com.mashup.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class ValidResponse(
    @field:Json(name = "valid")
    val valid: Boolean
)
