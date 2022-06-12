package com.mashup.ui.signup.dialog.platform

import androidx.fragment.app.activityViewModels
import com.mashup.R
import com.mashup.base.BaseBottomSheetDialogFragment
import com.mashup.databinding.DialogPlatformBinding
import com.mashup.ui.signup.SignUpViewModel
import kotlinx.coroutines.flow.collectLatest

class PlatformDialog : BaseBottomSheetDialogFragment<DialogPlatformBinding>() {

    private val viewModel: SignUpViewModel by activityViewModels()
    private val adapter: PlatformAdapter = PlatformAdapter(platformList) { platform ->
        viewModel.setPlatform(platform)
        dismiss()
    }

    override fun initViews() {
        setTitle("플랫폼 선택")
        initPlatFormRecyclerView()
    }

    override fun initObserves() {
        flowLifecycleScope {
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

    companion object {
        private val platformList = listOf(
            "Product Design", "Android", "iOS", "Web", "Spring", "Node"
        )
    }
}