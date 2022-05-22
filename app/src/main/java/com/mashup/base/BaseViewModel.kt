package com.mashup.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mashup.exceptions.NetworkException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

abstract class BaseViewModel : ViewModel() {

    private val _exceptionMessage = MutableSharedFlow<String>()
    val exceptionMessage: SharedFlow<String>
        get() = _exceptionMessage

    private val exceptionHandler =
        CoroutineExceptionHandler { _, throwable ->
            when (throwable) {
                is NetworkException -> {
                    _exceptionMessage.tryEmit(throwable.message)
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
}