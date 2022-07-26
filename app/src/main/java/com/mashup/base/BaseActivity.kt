package com.mashup.base

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
import com.mashup.network.NetworkStatusState
import com.mashup.network.data.NetworkStatusDetector
import com.mashup.network.errorcode.DISCONNECT_NETWORK
import com.mashup.network.errorcode.UNAUTHORIZED
import com.mashup.ui.error.NetworkDisconnectActivity
import com.mashup.ui.login.LoginActivity
import com.mashup.utils.keyboard.RootViewDeferringInsetsCallback
import com.mashup.widget.CommonDialog
import com.mashup.widget.MashUpToast
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

    val isConnectedNetwork: Boolean
        get() = networkStateDetector.hasNetworkConnection()

    protected val viewBinding: V by lazy {
        DataBindingUtil.inflate<V>(
            LayoutInflater.from(this), layoutId, null, false
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding.lifecycleOwner = this
        setContentView(viewBinding.root)
        WindowCompat.setDecorFitsSystemWindows(window, false)

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

    protected fun handleCommonError(code: String) {
        when (code) {
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

    protected fun showToast(text: String) {
        MashUpToast(applicationContext).run {
            setText(text)
            show()
        }
    }
}