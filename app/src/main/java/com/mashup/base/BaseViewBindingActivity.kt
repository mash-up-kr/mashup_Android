package com.mashup.base

import android.app.Dialog
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewbinding.ViewBinding
import com.mashup.constant.EXTRA_ACTIVITY_ENTER_TYPE
import com.mashup.constant.EXTRA_ANIMATION
import com.mashup.core.common.constant.BAD_REQUEST
import com.mashup.core.common.constant.DISCONNECT_NETWORK
import com.mashup.core.common.constant.INTERNAL_SERVER_ERROR
import com.mashup.core.common.constant.UNAUTHORIZED
import com.mashup.core.common.model.NavigationAnimationType
import com.mashup.core.common.utils.ProgressbarUtil
import com.mashup.core.common.utils.ToastUtil
import com.mashup.core.common.utils.keyboard.RootViewDeferringInsetsCallback
import com.mashup.core.common.widget.CommonDialog
import com.mashup.network.NetworkStatusState
import com.mashup.network.data.NetworkStatusDetector
import com.mashup.ui.error.NetworkDisconnectActivity
import com.mashup.ui.login.LoginActivity
import com.mashup.util.AnalyticsManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

abstract class BaseViewBindingActivity<V : ViewBinding> : AppCompatActivity() {
    private val networkStateDetector: NetworkStatusDetector by lazy {
        NetworkStatusDetector(
            context = this,
            coroutineScope = lifecycleScope
        )
    }

    private var animationType: NavigationAnimationType? = null

    val isConnectedNetwork: Boolean
        get() = networkStateDetector.hasNetworkConnection()

    private var loadingDialog: Dialog? = null

    abstract val viewBinding: V

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
        setContentView(viewBinding.root)

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.VANILLA_ICE_CREAM) {
            window.navigationBarColor = Color.TRANSPARENT
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            window.isNavigationBarContrastEnforced = false
        }

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

        initAnimationType()
        initWindowInset()
        initViews()
        initObserves()
    }

    open fun initViews() {
        /* explicitly empty */
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

    open fun onNetworkConnected() {
    }

    open fun onNetworkDisConnected() {
    }

    open fun initWindowInset() {
        val deferringInsetsListener = RootViewDeferringInsetsCallback(
            persistentInsetTypes = WindowInsetsCompat.Type.systemBars(),
            deferredInsetTypes = WindowInsetsCompat.Type.ime()
        )
        ViewCompat.setWindowInsetsAnimationCallback(viewBinding.root, deferringInsetsListener)
        ViewCompat.setOnApplyWindowInsetsListener(viewBinding.root, deferringInsetsListener)
    }

    private fun initAnimationType() {
        when (intent.getSerializableExtra(EXTRA_ANIMATION)) {
            NavigationAnimationType.SLIDE -> {
                animationType = NavigationAnimationType.SLIDE
            }
            NavigationAnimationType.PULL -> {
                animationType = NavigationAnimationType.PULL
            }
        }
        animationType?.run {
            overridePendingTransition(
                enterIn,
                enterOut
            )
        }
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
                            LoginActivity.newIntent(this@BaseViewBindingActivity)
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

    fun showLoading() {
        if (loadingDialog == null) {
            loadingDialog = ProgressbarUtil.show(this)
        }
    }

    fun hideLoading() {
        loadingDialog?.dismiss()
    }

    protected fun showToast(text: String) {
        ToastUtil.showToast(this, text)
    }

    override fun finish() {
        super.finish()
        animationType?.run {
            overridePendingTransition(
                0,
                exitOut
            )
        }
    }

    protected fun sendActivityEnterType(screenName: String) {
        val type = intent.getStringExtra(EXTRA_ACTIVITY_ENTER_TYPE) ?: return
        AnalyticsManager.addEvent(screenName, bundleOf("type" to type))
    }
}
