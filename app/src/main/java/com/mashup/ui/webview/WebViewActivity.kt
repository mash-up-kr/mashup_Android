package com.mashup.ui.webview

import android.content.Context
import android.content.Intent
import android.webkit.WebViewClient
import com.mashup.R
import com.mashup.base.BaseActivity
import com.mashup.databinding.ActivityWebViewBinding
import com.mashup.ui.constant.EXTRA_TITLE_KEY
import com.mashup.ui.constant.EXTRA_URL_KEY
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WebViewActivity : BaseActivity<ActivityWebViewBinding>() {

    private val title: String?
        get() = intent.getStringExtra(EXTRA_TITLE_KEY)
    private val webViewUrl: String?
        get() = intent.getStringExtra(EXTRA_URL_KEY)

    override fun initViews() {
        super.initViews()

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
        viewBinding.webView.run {
            settings.apply {
                loadWithOverviewMode = true
                webViewClient = WebViewClient()
                defaultTextEncodingName = "UTF-8"
            }
            webViewUrl?.run { loadUrl(this) }
        }
    }

    override val layoutId: Int
        get() = R.layout.activity_web_view

    companion object {

        fun newIntent(context: Context, title: String, url: String): Intent {
            return Intent(context, WebViewActivity::class.java).apply {
                putExtra(EXTRA_TITLE_KEY, title)
                putExtra(EXTRA_URL_KEY, url)
            }
        }
    }
}