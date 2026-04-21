package com.mashup.ui.webview.mashong

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.mashup.constant.EXTRA_TITLE_KEY
import com.mashup.core.common.base.BaseViewModel
import com.mashup.datastore.data.repository.UserPreferenceRepository
import com.mashup.ui.webview.WebViewUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MashongViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    userPreferenceRepository: UserPreferenceRepository
) : BaseViewModel() {

    private val showDividerFlow = MutableStateFlow(false)

    val webViewUiState = combine(
        savedStateHandle.getStateFlow(EXTRA_TITLE_KEY, ""),
        savedStateHandle.getStateFlow(EXTRA_MASHONG_BASE_URL_KEY, ""),
        showDividerFlow,
        userPreferenceRepository.getUserPreference()
    ) { title, baseUrl, showDivider, prefs ->
        WebViewUiState.Success(
            title = title,
            webViewUrl = baseUrl + prefs.platform,
            showToolbarDivider = showDivider,
            additionalHttpHeaders = mapOf("authorization" to prefs.token)
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        WebViewUiState.Loading
    )

    override fun handleErrorCode(code: String) {}

    companion object {
        const val EXTRA_MASHONG_BASE_URL_KEY = "extra_mashong_base_url_key"
    }
}
