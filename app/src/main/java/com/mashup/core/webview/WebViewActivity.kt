package com.mashup.core.webview

import android.content.Context
import android.content.Intent
import android.webkit.WebViewClient
import com.mashup.R
import com.mashup.base.BaseActivity
import com.mashup.common.extensions.setStatusBarColorRes
import com.mashup.common.model.NavigationAnimationType
import com.mashup.constant.EXTRA_ANIMATION
import com.mashup.constant.EXTRA_TITLE_KEY
import com.mashup.constant.EXTRA_URL_KEY
import com.mashup.databinding.ActivityWebViewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WebViewActivity : BaseActivity<ActivityWebViewBinding>() {

    private val title: String?
        get() = intent.getStringExtra(EXTRA_TITLE_KEY)
    private val webViewUrl: String?
        get() = intent.getStringExtra(EXTRA_URL_KEY)

    override fun initViews() {
        super.initViews()
        setStatusBarColorRes(R.color.white)
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
            setOnScrollChangeListener { view, _, _, _, _ ->
                if (view.canScrollVertically(-1)) {
                    viewBinding.toolbar.showDivider()
                } else {
                    viewBinding.toolbar.hideDivider()
                }
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
