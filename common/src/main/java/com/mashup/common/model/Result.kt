package com.mashup.common.model

sealed interface Result<out T> {
    data class Success<T>(val data: T) : Result<T>
    data class Error(val code: String, val exception: Throwable? = null) : Result<Nothing>
    object Loading : Result<Nothing>
}
