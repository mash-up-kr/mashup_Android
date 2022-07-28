package com.mashup.ui.setting

import androidx.fragment.app.activityViewModels
import com.mashup.R
import com.mashup.base.BaseFragment
import com.mashup.common.Validation
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
            closeFragment()
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
                Validation.SUCCESS -> {
                    setSuccessUiOfTextField()
                }
                Validation.FAILED -> {
                    setDescriptionText("문구가 동일하지 않아요")
                    setFailedUiOfTextField()
                }
                Validation.EMPTY -> {
                    setEmptyUIOfTextField()
                }
            }
        }
        viewBinding.btnWithdrawal.setButtonEnabled(codeState.isValidationState)
    }


    private fun closeFragment() {
        val fragmentManager = activity?.supportFragmentManager
        fragmentManager?.let {
            it.beginTransaction().remove(this@WithdrawalFragment).commit()
            it.popBackStack()
        }
    }

    companion object {
        fun newInstance() = WithdrawalFragment()
    }

    override val layoutId: Int = R.layout.fragment_withdrawal

}