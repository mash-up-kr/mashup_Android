package com.mashup.base

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.mashup.core.common.constant.BAD_REQUEST
import com.mashup.core.common.constant.DISCONNECT_NETWORK
import com.mashup.core.common.constant.INTERNAL_SERVER_ERROR
import com.mashup.core.common.constant.UNAUTHORIZED
import com.mashup.core.common.utils.ProgressbarUtil
import com.mashup.core.common.utils.ToastUtil
import com.mashup.core.common.widget.CommonDialog
import com.mashup.network.NetworkStatusState
import com.mashup.network.data.NetworkStatusDetector
import com.mashup.ui.error.NetworkDisconnectActivity
import com.mashup.ui.login.LoginActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

open class BaseComposeFragment : Fragment() {
    private val networkStateDetector: NetworkStatusDetector by lazy {
        NetworkStatusDetector(
            context = requireContext(),
            coroutineScope = lifecycleScope
        )
    }

    val isConnectedNetwork: Boolean
        get() = networkStateDetector.hasNetworkConnection()

    private var loadingDialog: Dialog? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserves()
    }

    open fun initObserves() {
        flowViewLifecycleScope {
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

    protected fun flowViewLifecycleScope(
        state: Lifecycle.State = Lifecycle.State.STARTED,
        block: suspend CoroutineScope.() -> Unit
    ) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(state) {
                block.invoke(this)
            }
        }
    }

    open fun onNetworkConnected() {
    }

    open fun onNetworkDisConnected() {
    }

    protected fun handleCommonError(code: String) {
        when (code) {
            BAD_REQUEST, INTERNAL_SERVER_ERROR -> {
                showToast("잠시 후 다시 시도해주세요.")
            }
            UNAUTHORIZED -> {
                CommonDialog(requireContext()).apply {
                    setTitle(text = "주의")
                    setMessage(text = "인증정보가 초기화되어 재로그인이 필요합니다")
                    setPositiveButton {
                        requireActivity().run {
                            startActivity(
                                LoginActivity.newIntent(this)
                            )
                            finish()
                        }
                    }
                    show()
                }
            }
            DISCONNECT_NETWORK -> {
                requireActivity().startActivity(
                    NetworkDisconnectActivity.newIntent(requireContext())
                )
            }
        }
    }

    protected fun showToast(text: String) {
        ToastUtil.showToast(requireContext(), text)
    }

    fun showLoading() {
        if (loadingDialog == null) {
            loadingDialog = ProgressbarUtil.show(requireContext())
        }
    }

    fun hideLoading() {
        loadingDialog?.dismiss()
    }
}
