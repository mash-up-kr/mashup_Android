package com.mashup.ui.webview

import android.content.Context
import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import com.google.accompanist.web.AccompanistWebChromeClient
import com.google.accompanist.web.AccompanistWebViewClient
import com.google.accompanist.web.WebView
import com.google.accompanist.web.WebViewNavigator
import com.google.accompanist.web.WebViewState
import com.google.accompanist.web.rememberWebViewNavigator
import com.google.accompanist.web.rememberWebViewState
import com.mashup.R
import com.mashup.base.BaseActivity
import com.mashup.constant.EXTRA_ANIMATION
import com.mashup.constant.EXTRA_TITLE_KEY
import com.mashup.constant.EXTRA_URL_KEY
import com.mashup.core.common.extensions.setStatusBarColorRes
import com.mashup.core.common.model.NavigationAnimationType
import com.mashup.databinding.ActivityWebViewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WebViewActivity : BaseActivity<ActivityWebViewBinding>() {

    private val title: String?
        get() = intent.getStringExtra(EXTRA_TITLE_KEY)
    private val webViewUrl: String?
        get() = intent.getStringExtra(EXTRA_URL_KEY)

    private val webViewClient = AccompanistWebViewClient()
    private val webChromeClient = AccompanistWebChromeClient()

    override fun initViews() {
        super.initViews()
        setStatusBarColorRes(com.mashup.core.common.R.color.white)
        initWindowInset()

        initToolbar()
        initWebView()
    }

    private fun initToolbar() {
        viewBinding.toolbar.run {
            title?.run { setTitle(this) }
            setOnCloseButtonClickListener {
                finish()
            }
        }
    }

    private fun initWebView() {
        viewBinding.webView.setContent {
            val webViewState = rememberWebViewState(url = webViewUrl ?: "")
            val webViewNavigator = rememberWebViewNavigator()
            WebViewSetting(webViewState, webViewNavigator)
        }
    }

    @Composable
    private fun WebViewSetting(webViewState: WebViewState, webViewNavigator: WebViewNavigator) {
        WebView(
            state = webViewState,
            client = webViewClient,
            chromeClient = webChromeClient,
            navigator = webViewNavigator,
            onCreated = { webView ->
                with(webView) {
                    settings.run {
                        domStorageEnabled = true
                        loadWithOverviewMode = true
                        defaultTextEncodingName = "UTF-8"
                    }
                    setOnScrollChangeListener { view, _, _, _, _ ->
                        if (view.canScrollVertically(-1)) {
                            viewBinding.toolbar.showDivider()
                        } else {
                            viewBinding.toolbar.hideDivider()
                        }
                    }
                }
            },
        )

        BackHandler(enabled = true) {
            // 페이지가 단 한개라서... 뒤로가기 기능도 넣긴 했습니다 큰 기능이 없어서 언제나 뒤로가기 가능하게끔
            if (webViewNavigator.canGoBack) {
                finish()
            } else {
                finish()
            }
        }
    }

    override val layoutId: Int
        get() = R.layout.activity_web_view

    companion object {

        fun newIntent(context: Context, title: String, url: String): Intent {
            return Intent(context, WebViewActivity::class.java).apply {
                putExtra(EXTRA_ANIMATION, NavigationAnimationType.PULL)
                putExtra(EXTRA_TITLE_KEY, title)
                putExtra(EXTRA_URL_KEY, url)
            }
        }
    }
}
