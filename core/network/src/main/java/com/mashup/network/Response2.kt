package com.mashup.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * TODO 네이밍 변경 요망!!
 */
@JsonClass(generateAdapter = true)
data class Response2<T>(
    @field:Json(name = "code")
    val code: String,
    @field:Json(name = "message")
    val message: String?,
    @field:Json(name = "data")
    val data: T?,
    @field:Json(name = "page")
    val page: PageResponse2?
) {
    fun isSuccess() = code == "SUCCESS"
}

@JsonClass(generateAdapter = true)
data class PageResponse2(
    @field:Json(name = "number")
    val number: Int,
    @field:Json(name = "size")
    val size: Int,
    @field:Json(name = "totalCount")
    val totalCount: Int
)
