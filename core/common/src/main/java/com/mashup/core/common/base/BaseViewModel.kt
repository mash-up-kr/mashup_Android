package com.mashup.core.common.base

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mashup.core.common.constant.BAD_REQUEST
import com.mashup.core.common.constant.DISCONNECT_NETWORK
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import java.io.EOFException
import java.net.UnknownHostException
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

abstract class BaseViewModel : ViewModel() {

    protected val _errorCode = MutableSharedFlow<String>()
    open val errorCode: SharedFlow<String> = _errorCode

    private val exceptionHandler =
        CoroutineExceptionHandler { _, throwable ->
            Log.e("coroutine throwable", throwable.message.orEmpty())
            when (throwable) {
                is UnknownHostException, is EOFException -> {
                    handleErrorCode(DISCONNECT_NETWORK)
                }
                else -> {
                    handleErrorCode(BAD_REQUEST)
                }
            }
        }

    protected fun mashUpScope(
        context: CoroutineContext = EmptyCoroutineContext,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> Unit
    ) {
        viewModelScope.launch(context + exceptionHandler, start) {
            block.invoke(this)
        }
    }

    open fun handleErrorCode(code: String) = mashUpScope {
        _errorCode.emit(code)
    }
}
