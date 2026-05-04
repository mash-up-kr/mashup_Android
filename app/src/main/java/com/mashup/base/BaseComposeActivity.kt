package com.mashup.base

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.content.IntentCompat
import androidx.core.os.bundleOf
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.mashup.constant.EXTRA_ACTIVITY_ENTER_TYPE
import com.mashup.constant.EXTRA_ANIMATION
import com.mashup.core.common.constant.BAD_REQUEST
import com.mashup.core.common.constant.DISCONNECT_NETWORK
import com.mashup.core.common.constant.INTERNAL_SERVER_ERROR
import com.mashup.core.common.constant.UNAUTHORIZED
import com.mashup.core.common.model.NavigationAnimationType
import com.mashup.core.common.utils.ProgressDialogContainer
import com.mashup.core.common.utils.ToastUtil
import com.mashup.core.common.widget.CommonDialog
import com.mashup.core.ui.theme.MashUpTheme
import com.mashup.network.NetworkStatusState
import com.mashup.network.data.NetworkStatusDetector
import com.mashup.ui.error.NetworkDisconnectActivity
import com.mashup.ui.login.LoginActivity
import com.mashup.util.AnalyticsManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

open class BaseComposeActivity : ComponentActivity() {
    private val networkStateDetector: NetworkStatusDetector by lazy {
        NetworkStatusDetector(
            context = this,
            coroutineScope = lifecycleScope
        )
    }

    val isConnectedNetwork: Boolean
        get() = networkStateDetector.hasNetworkConnection()

    private var animationType: NavigationAnimationType? = null

    private val loadingDialogContainer = ProgressDialogContainer()

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(
                Color.TRANSPARENT,
                Color.TRANSPARENT
            ),
            navigationBarStyle = SystemBarStyle.auto(
                Color.TRANSPARENT,
                Color.TRANSPARENT
            )
        )
        super.onCreate(savedInstanceState)
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> {
                window.insetsController?.setSystemBarsAppearance(
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS or
                        WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS,
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS or
                        WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS
                )
            }

            Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> {
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or
                    View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
            }

            else -> {
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.VANILLA_ICE_CREAM) {
            window.navigationBarColor = Color.TRANSPARENT
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            window.isNavigationBarContrastEnforced = false
        }

        initView()
        initAnimationType()
        initObserves()

        setContent {
            MashUpTheme {
                MainContainer(
                    modifier = Modifier.fillMaxSize()
                        .statusBarsPadding()
                        .navigationBarsPadding()
                )
            }
        }
    }

    @Composable
    open fun MainContainer(modifier: Modifier) {
        /* explicitly empty */
    }

    override fun finish() {
        super.finish()
        animationType?.run {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                overrideActivityTransition(
                    OVERRIDE_TRANSITION_CLOSE,
                    0,
                    exitOut
                )
            } else {
                @Suppress("DEPRECATION")
                overridePendingTransition(
                    0,
                    exitOut
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        loadingDialogContainer.clear()
    }

    open fun initView() {
        /* explicitly empty */
    }

    private fun initAnimationType() {
        animationType = IntentCompat.getSerializableExtra(
            intent,
            EXTRA_ANIMATION,
            NavigationAnimationType::class.java
        )
        animationType?.run {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                overrideActivityTransition(
                    OVERRIDE_TRANSITION_OPEN,
                    enterIn,
                    enterOut
                )
            } else {
                @Suppress("DEPRECATION")
                overridePendingTransition(
                    enterIn,
                    enterOut
                )
            }
        }
    }

    open fun initObserves() {
        flowLifecycleScope {
            networkStateDetector.state.collectLatest { networkState ->
                when (networkState) {
                    is NetworkStatusState.NetworkConnected -> {
                        onNetworkConnected()
                    }

                    is NetworkStatusState.NetworkDisconnected -> {
                        onNetworkDisConnected()
                    }
                }
            }
        }
    }

    protected fun flowLifecycleScope(
        state: Lifecycle.State = Lifecycle.State.STARTED,
        block: suspend CoroutineScope.() -> Unit
    ) {
        lifecycleScope.launch {
            repeatOnLifecycle(state) {
                block.invoke(this)
            }
        }
    }

    open fun onNetworkConnected() {
    }

    open fun onNetworkDisConnected() {
    }

    open fun handleCommonError(code: String) {
        when (code) {
            BAD_REQUEST, INTERNAL_SERVER_ERROR -> {
                showToast("잠시 후 다시 시도해주세요.")
            }

            UNAUTHORIZED -> {
                CommonDialog(this).apply {
                    setTitle(text = "주의")
                    setMessage(text = "인증정보가 초기화되어 재로그인이 필요합니다")
                    setPositiveButton {
                        startActivity(
                            LoginActivity.newIntent(this@BaseComposeActivity)
                        )
                        finish()
                    }
                    show()
                }
            }

            DISCONNECT_NETWORK -> {
                startActivity(
                    NetworkDisconnectActivity.newIntent(this)
                )
            }
        }
    }

    protected fun showToast(text: String) {
        ToastUtil.showToast(this, text)
    }

    fun showLoading() = loadingDialogContainer.showLoading(this)

    fun hideLoading() = loadingDialogContainer.hideLoading()

    protected fun sendActivityEnterType(screenName: String) {
        val type = intent.getStringExtra(EXTRA_ACTIVITY_ENTER_TYPE) ?: return
        AnalyticsManager.addEvent(screenName, bundleOf("type" to type))
    }
}
