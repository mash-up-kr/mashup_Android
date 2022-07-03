package com.mashup.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiResponse<D>(
    val code: String,
    val message: String?,
    val data: D?,
    val page: ApiPageResponse?
)

@JsonClass(generateAdapter = true)
data class ApiPageResponse(
    val number: Int,
    val size: Int,
    val totalCount: Int
)