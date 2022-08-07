package com.mashup.base

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.mashup.network.NetworkStatusState
import com.mashup.network.data.NetworkStatusDetector
import com.mashup.network.errorcode.BAD_REQUEST
import com.mashup.network.errorcode.DISCONNECT_NETWORK
import com.mashup.network.errorcode.UNAUTHORIZED
import com.mashup.ui.error.NetworkDisconnectActivity
import com.mashup.ui.login.LoginActivity
import com.mashup.utils.ProgressbarUtil
import com.mashup.utils.ToastUtil
import com.mashup.widget.CommonDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

abstract class BaseFragment<V : ViewDataBinding> : Fragment() {
    private var _viewBinding: V? = null
    protected val viewBinding: V
        get() = _viewBinding!!

    abstract val layoutId: Int

    private var loadingDialog: Dialog? = null


    private val networkStateDetector: NetworkStatusDetector by lazy {
        NetworkStatusDetector(
            context = requireContext(),
            coroutineScope = lifecycleScope
        )
    }

    val isConnectedNetwork: Boolean
        get() = networkStateDetector.hasNetworkConnection()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _viewBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(requireContext()), layoutId, null, false
            )
        viewBinding.lifecycleOwner = viewLifecycleOwner
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initObserves()
    }

    open fun initViews() {
        /* explicitly empty */
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

    open fun onNetworkConnected() {
    }

    open fun onNetworkDisConnected() {
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }

    protected fun handleCommonError(code: String) {
        when (code) {
            BAD_REQUEST -> {
                CommonDialog(requireContext()).apply {
                    setTitle(text = "오류 발생")
                    setMessage(text = "잠시 후 다시 시도해주세요.")
                    setPositiveButton()
                    show()
                }
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

    fun showLoading() {
        if (loadingDialog == null) {
            loadingDialog = ProgressbarUtil.show(requireContext())
        }
    }

    fun hideLoading() {
        loadingDialog?.dismiss()
    }

    protected fun isViewBindingExist() = _viewBinding != null

    protected fun showToast(text: String) {
        ToastUtil.showToast(requireContext(), text)
    }
}