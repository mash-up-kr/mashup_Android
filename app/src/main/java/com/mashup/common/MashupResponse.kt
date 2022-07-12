package com.mashup.common

open class Response<T>(
    val code: String,
    val message: String?,
    val data: T?
)
