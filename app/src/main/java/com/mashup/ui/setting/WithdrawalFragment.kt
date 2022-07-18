package com.mashup.ui.setting

import androidx.fragment.app.activityViewModels
import com.mashup.R
import com.mashup.base.BaseFragment
import com.mashup.databinding.FragmentWithdrawalBinding
import com.mashup.ui.extensions.setEmptyUIOfTextField
import com.mashup.ui.extensions.setFailedUiOfTextField
import com.mashup.ui.extensions.setSuccessUiOfTextField
import com.mashup.ui.signup.state.CodeState
import kotlinx.coroutines.flow.collectLatest

class WithdrawalFragment : BaseFragment<FragmentWithdrawalBinding>() {

    private val viewModel: SettingViewModel by activityViewModels()

    override fun initViews() {
        initTextField()
        initButton()
    }

    private fun initButton() {
        viewBinding.toolbar.setOnBackButtonClickListener {
        }
    }

    private fun initTextField() {
        viewBinding.textFieldCode.run {
            addOnTextChangedListener { text ->
                viewModel.setCode(text)
            }
        }
    }

    override fun initObserves() = with(viewModel) {
        flowViewLifecycleScope {
            viewModel.codeState.collectLatest {
                setUiOfCodeState(it)
            }
        }
    }


    private fun setUiOfCodeState(codeState: CodeState) {
        with(viewBinding.textFieldCode) {
            when (codeState.validationCode) {
                com.mashup.ui.model.Validation.SUCCESS -> {
                    setSuccessUiOfTextField()
                }
                com.mashup.ui.model.Validation.FAILED -> {
                    setDescriptionText("문구가 동일하지 않아요")
                    setFailedUiOfTextField()
                }
                com.mashup.ui.model.Validation.EMPTY -> {
                    setEmptyUIOfTextField()
                }
            }
        }
        viewBinding.btnWithdrawal.setButtonEnabled(codeState.isValidationState)
    }

    companion object {
        fun newInstance() = WithdrawalFragment()
    }

    override val layoutId: Int = R.layout.fragment_withdrawal

}