package com.mashup.ui.signup.dialog.term

import androidx.fragment.app.activityViewModels
import com.mashup.R
import com.mashup.base.BaseBottomSheetDialogFragment
import com.mashup.databinding.DialogTermsAgreementBinding
import com.mashup.extensions.flowViewLifecycleScope
import com.mashup.extensions.setUnderLine
import com.mashup.ui.signup.SignUpViewModel
import kotlinx.coroutines.flow.collectLatest

class TermsAgreementDialog : BaseBottomSheetDialogFragment<DialogTermsAgreementBinding>() {

    private val viewModel: SignUpViewModel by activityViewModels()

    override fun initViews() {
        super.initViews()
        setTitle("필수약관동의")
        setVisibleCloseButton(true)

        viewBinding.viewClickableSpace.setOnClickListener {
            viewModel.updatedTerm()
        }
        initDetailTextView()
    }

    private fun initDetailTextView() {
        viewBinding.tvDetail.setUnderLine()
    }

    override fun initObserves() {
        flowViewLifecycleScope {
            viewModel.isCheckedTerm.collectLatest { isChecked ->
                viewBinding.icPersonalAgreement.setImageResource(
                    if (isChecked) {
                        R.drawable.ic_circle_checked
                    } else {
                        R.drawable.ic_circle_not_checked
                    }
                )
                viewBinding.btnConfirm.setButtonEnabled(isChecked)
            }
        }
    }


    override val layoutId: Int
        get() = R.layout.dialog_terms_agreement
}