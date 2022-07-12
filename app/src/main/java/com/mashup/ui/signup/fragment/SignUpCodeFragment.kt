package com.mashup.ui.signup.fragment

import androidx.fragment.app.activityViewModels
import com.mashup.R
import com.mashup.base.BaseFragment
import com.mashup.databinding.FragmentSignUpCodeBinding
import com.mashup.ui.extensions.setEmptyUIOfTextField
import com.mashup.ui.extensions.setFailedUiOfTextField
import com.mashup.ui.extensions.setSuccessUiOfTextField
import com.mashup.ui.model.Validation
import com.mashup.ui.signup.SignUpViewModel
import com.mashup.ui.signup.state.CodeState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class SignUpCodeFragment : BaseFragment<FragmentSignUpCodeBinding>() {

    private val viewModel: SignUpViewModel by activityViewModels()

    override val layoutId: Int
        get() = R.layout.fragment_sign_up_code

    override fun initViews() {
        initTextField()
        initButton()
    }

    override fun initObserves() = with(viewModel) {
        flowViewLifecycleScope {
            viewModel.codeState.collectLatest {
                setUiOfCodeState(it)
            }
        }
    }

    private fun initTextField() {
        viewBinding.textFieldCode.run {
            addOnTextChangedListener { text ->
                viewModel.setCode(text)
            }
        }
    }

    private fun initButton() {
        viewBinding.btnSignUp.setOnButtonClickListener {
        }
    }

    private fun setUiOfCodeState(codeState: CodeState) {
        with(viewBinding.textFieldCode) {
            when (codeState.validationCode) {
                Validation.SUCCESS -> {
                    setDescriptionText("")
                    setSuccessUiOfTextField()
                }
                Validation.FAILED -> {
                    setDescriptionText("가입코드가 일치하지 않아요")
                    setFailedUiOfTextField()
                }
                Validation.EMPTY -> {
                    setDescriptionText("")
                    setEmptyUIOfTextField()
                }
            }
        }
        viewBinding.btnSignUp.setButtonEnabled(codeState.isValidationState)
    }
}