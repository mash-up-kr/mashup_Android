package com.mashup.ui.error.vm

import com.mashup.core.common.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class NetworkDisconnectViewModel
@Inject
constructor(
    // Not Inject
) : BaseViewModel() {
    sealed interface Effect {
        data object FinishView : Effect
    }

    private val _loadingState: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val loadingState: StateFlow<Boolean>
        get() = _loadingState.asStateFlow()

    private val _sideEffect: MutableSharedFlow<Effect> = MutableSharedFlow()
    val sideEffect: SharedFlow<Effect>
        get() = _sideEffect.asSharedFlow()

    fun onClickRetry() =
        mashUpScope {
            _loadingState.emit(true)

            _sideEffect.emit(Effect.FinishView)

            _loadingState.emit(false)
        }
}
