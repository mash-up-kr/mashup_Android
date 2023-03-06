package com.mashup.ui.webview

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.mashup.base.BaseViewModel
import com.mashup.constant.EXTRA_TITLE_KEY
import com.mashup.constant.EXTRA_URL_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class WebViewViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    private val showDividerFlow = MutableStateFlow(false)

    val webViewUiState = combine(
        savedStateHandle.getStateFlow(EXTRA_TITLE_KEY, ""),
        savedStateHandle.getStateFlow(EXTRA_URL_KEY, ""),
        showDividerFlow
    ) { title, webViewUrl, showDivider ->
        WebViewUiState.Success(
            title = title,
            webViewUrl = webViewUrl,
            showToolbarDivider = showDivider
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        WebViewUiState.Loading
    )

    fun onWebViewScroll(isScrollTop: Boolean) = mashUpScope {
        showDividerFlow.emit(isScrollTop.not())
    }

    override fun handleErrorCode(code: String) {
    }
}

sealed interface WebViewUiState {
    object Loading : WebViewUiState

    data class Success(
        val title: String,
        val webViewUrl: String,
        val showToolbarDivider: Boolean
    ) : WebViewUiState
}