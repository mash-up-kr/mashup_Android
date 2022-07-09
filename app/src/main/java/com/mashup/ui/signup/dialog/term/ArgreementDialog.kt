package com.mashup.ui.signup.dialog.term

import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.mashup.R
import com.mashup.base.BaseBottomSheetDialogFragment
import com.mashup.databinding.DialogTermsAgreementBinding
import com.mashup.extensions.onDebouncedClick
import com.mashup.extensions.setUnderLine
import com.mashup.ui.signup.SignUpViewModel
import com.mashup.ui.webview.WebViewActivity
import kotlinx.coroutines.flow.collectLatest

class TermsAgreementDialog : BaseBottomSheetDialogFragment<DialogTermsAgreementBinding>() {

    private val viewModel: SignUpViewModel by activityViewModels()

    override fun initViews() {
        super.initViews()
        setTitle("필수약관동의")
        setVisibleCloseButton(true)

        initThermItem()
        initDetailTextView()
    }

    private fun initThermItem() {
        viewBinding.termClickableSpace.setOnClickListener {
            viewModel.updatedTerm()
        }
        viewBinding.tvDetail.onDebouncedClick(viewLifecycleOwner.lifecycleScope) {
            context?.run {
                startActivity(
                    WebViewActivity.newIntent(
                        context = this,
                        title = "개인정보방침",
                        url = "https://www.naver.com"
                    )
                )
                viewModel.updatedTerm(true)
            }
        }
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