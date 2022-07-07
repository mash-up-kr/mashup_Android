package com.mashup.common.result

import android.util.Log
import kotlinx.coroutines.CancellationException

sealed interface Result<out T> {
    data class Success<T>(val data: T) : Result<T>
    data class Error(val exception: Throwable? = null) : Result<Nothing>
    object Loading : Result<Nothing>
}

suspend fun <T> suspendRunCatching(block: suspend () -> T): Result<T> = try {
    Result.Success(block())
} catch (cancellationException: CancellationException) {
    throw cancellationException
} catch (exception: Exception) {
    Log.i(
        "suspendRunCatching",
        "Failed to evaluate a suspendRunCatchingBlock. Returning failure Result",
        exception
    )
    Result.Error(exception)
}
