package com.mashup.data

import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse<D>(
    val code: String,
    val message: String?,
    val data: D?,
    val page: ApiPageResponse?
)

@Serializable
data class ApiPageResponse(
    val number: Int,
    val size: Int,
    val totalCount: Int
)