package com.mashup.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Response<T>(
    @field:Json(name = "code")
    val code: String,
    @field:Json(name = "message")
    val message: String?,
    @field:Json(name = "data")
    val data: T?,
    @field:Json(name = "page")
    val page: PageResponse?
) {
    fun isSuccess() = code == "SUCCESS"
}

@JsonClass(generateAdapter = true)
data class PageResponse(
    @field:Json(name = "number")
    val number: Int,
    @field:Json(name = "size")
    val size: Int,
    @field:Json(name = "totalCount")
    val totalCount: Int
)