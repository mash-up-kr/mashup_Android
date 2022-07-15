package com.mashup.common

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Response<T>(
    @field:Json(name = "code")
    val code: String,
    @field:Json(name = "message")
    val message: String?,
    @field:Json(name = "data")
    val data: T?
) {
    fun isSuccess() = code == "SUCCESS"
}
