package com.mashup.ui.webview.mashong

import androidx.lifecycle.viewModelScope
import com.mashup.core.common.base.BaseViewModel
import com.mashup.data.network.WEB_HOST
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
    userPreferenceRepository: UserPreferenceRepository
) : BaseViewModel() {

    private val showDividerFlow = MutableStateFlow(false)

    val webViewUiState = combine(
        showDividerFlow,
        userPreferenceRepository.getUserPreference()
    ) { showDivider, prefs ->
        WebViewUiState.Success(
            title = MASHONG_TITLE,
            webViewUrl = MASHONG_BASE_URL + prefs.platform,
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
        private const val MASHONG_TITLE = "mashong"
        private const val MASHONG_BASE_URL = WEB_HOST + "mashong/"
    }
}
