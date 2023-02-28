package com.mashup.ui.webview

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewNavigator
import com.google.accompanist.web.rememberWebViewState
import com.mashup.core.ui.widget.MashUpToolbar

@Composable
fun WebViewScreen(
    modifier: Modifier = Modifier,
    webViewUiState: WebViewUiState,
    isScrollTop: (Boolean) -> Unit = {},
    onBackPressed: () -> Unit
) {
    Column(modifier = modifier) {
        MashUpToolbar(
            modifier = Modifier.fillMaxWidth(),
            title = (webViewUiState as? WebViewUiState.Success)?.title
                ?: "",
            showBackButton = true,
            showBottomDivider = (webViewUiState as? WebViewUiState.Success)?.showToolbarDivider
                ?: false,
            onClickBackButton = onBackPressed
        )
        if (webViewUiState is WebViewUiState.Success) {
            MashUpWebView(
                webViewUrl = webViewUiState.webViewUrl,
                isScrollTop = isScrollTop,
                onBackPressed = onBackPressed
            )
        }
    }
}

@Composable
private fun MashUpWebView(
    webViewUrl: String?,
    isScrollTop: (Boolean) -> Unit = {},
    onBackPressed: () -> Unit
) {
    val webViewState = rememberWebViewState(url = webViewUrl ?: "")
    val webViewNavigator = rememberWebViewNavigator()

    WebView(
        state = webViewState,
        navigator = webViewNavigator,
        onCreated = { webView ->
            with(webView) {
                settings.run {
                    domStorageEnabled = true
                    loadWithOverviewMode = true
                    defaultTextEncodingName = "UTF-8"
                }
                setOnScrollChangeListener { view, _, _, _, _ ->
                    isScrollTop(!view.canScrollVertically(-1))
                }
            }
        }
    )

    BackHandler(enabled = true) {
//        // 페이지가 단 한개라서... 뒤로가기 기능도 넣긴 했습니다 큰 기능이 없어서 언제나 뒤로가기 가능하게끔
//        if (webViewNavigator.canGoBack) {
//            onBackPressed()
//        } else {
//            onBackPressed()
//        }
        onBackPressed()
    }
}