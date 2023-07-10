package com.mashup.core.network.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class StorageResponse(
    @field:Json(name = "keyString")
    val keyString: String,
    @field:Json(name = "valueMap")
    val valueMap: Map<String, String>
)
