package com.mashup.ui.signup.dialog.platform

import androidx.fragment.app.activityViewModels
import com.mashup.R
import com.mashup.base.BaseBottomSheetDialogFragment
import com.mashup.databinding.DialogPlatformBinding
import com.mashup.ui.model.Platform
import com.mashup.ui.signup.SignUpViewModel
import kotlinx.coroutines.flow.collectLatest

class PlatformDialog : BaseBottomSheetDialogFragment<DialogPlatformBinding>() {

    private val viewModel: SignUpViewModel by activityViewModels()
    private val adapter: PlatformAdapter =
        PlatformAdapter(
            Platform.values().filter { it != Platform.NONE }.toList()
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
