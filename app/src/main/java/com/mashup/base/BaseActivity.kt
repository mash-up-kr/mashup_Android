package com.mashup.base

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

abstract class BaseActivity<V : ViewDataBinding> : AppCompatActivity() {
    abstract val layoutId: Int

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

    protected val viewBinding: V by lazy {
        DataBindingUtil.inflate<V>(
            LayoutInflater.from(this),
            layoutId,
            null,
            false
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding.lifecycleOwner = this
        setContentView(viewBinding.root)
        WindowCompat.setDecorFitsSystemWindows(window, false)

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

    protected fun handleCommonError(code: String) {
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
                            LoginActivity.newIntent(this@BaseActivity)
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
}
