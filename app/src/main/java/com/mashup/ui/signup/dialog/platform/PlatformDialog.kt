package com.mashup.ui.signup.dialog.platform

import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.activityViewModels
import com.mashup.R
import com.mashup.base.BaseBottomSheetDialogFragment
import com.mashup.core.common.utils.keyboard.RootViewDeferringInsetsCallback
import com.mashup.core.model.Platform
import com.mashup.databinding.DialogPlatformBinding
import com.mashup.ui.signup.SignUpViewModel
import kotlinx.coroutines.flow.collectLatest

class PlatformDialog : BaseBottomSheetDialogFragment<DialogPlatformBinding>() {
    // 텍스트 입력 없으므로 IME 처리 불필요
    override fun initWindowInset() {
        val deferringInsetsListener = RootViewDeferringInsetsCallback(
            persistentInsetTypes = WindowInsetsCompat.Type.navigationBars(),
            deferredInsetTypes = 0 // IME 처리 안 함
        )
        rootViewBinding.let { binding ->
            ViewCompat.setWindowInsetsAnimationCallback(binding.root, deferringInsetsListener)
            ViewCompat.setOnApplyWindowInsetsListener(binding.root, deferringInsetsListener)
        }
    }
    private val viewModel: SignUpViewModel by activityViewModels()
    private val adapter: PlatformAdapter =
        PlatformAdapter(
            Platform.values().filter { it != Platform.UNKNOWN }.toList()
        ) { platform ->
            viewModel.setPlatform(platform)
            dismiss()
        }

    override fun initViews() {
        super.initViews()
        setTitle("플랫폼 선택")
        setVisibleCloseButton(true)
        initPlatFormRecyclerView()
    }

    override fun initObserves() {
        flowViewLifecycleScope {
            viewModel.platform.collectLatest {
                adapter.updateSelectedPlatform(it)
            }
        }
    }

    private fun initPlatFormRecyclerView() {
        viewBinding.rvPlatform.adapter = adapter
    }

    override val layoutId: Int
        get() = R.layout.dialog_platform
}
