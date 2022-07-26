package com.mashup.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mashup.network.errorcode.DISCONNECT_NETWORK
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

    private val _exceptionMessage = MutableSharedFlow<String>()
    val exceptionMessage: SharedFlow<String>
        get() = _exceptionMessage

    private val exceptionHandler =
        CoroutineExceptionHandler { _, throwable ->
            when (throwable) {
                is UnknownHostException, is EOFException -> {
                    handleErrorCode(DISCONNECT_NETWORK)
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

    abstract fun handleErrorCode(code: String)
}