package com.mashup.ui.signup.fragment

import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.activityViewModels
import com.mashup.R
import com.mashup.base.BaseFragment
import com.mashup.databinding.FragmentSignUpCodeBinding
import com.mashup.network.errorcode.INVALID_PLATFORM_NAME
import com.mashup.network.errorcode.MEMBER_INVALID_INVITE
import com.mashup.ui.extensions.setEmptyUIOfTextField
import com.mashup.ui.extensions.setFailedUiOfTextField
import com.mashup.ui.extensions.setSuccessUiOfTextField
import com.mashup.ui.model.Validation
import com.mashup.ui.signup.SignUpState
import com.mashup.ui.signup.SignUpViewModel
import com.mashup.ui.signup.state.CodeState
import com.mashup.utils.keyboard.TranslateDeferringInsetsAnimationCallback
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
            codeState.collectLatest {
                setUiOfCodeState(it)
            }
        }

        flowViewLifecycleScope {
            signUpState.collectLatest { state ->
                when (state) {
                    SignUpState.InvalidCode -> {
                        viewBinding.textFieldCode.run {
                            setDescriptionText("가입코드가 일치하지 않아요")
                            setFailedUiOfTextField()
                        }
                    }
                    SignUpState.SUCCESS -> {
                        requireActivity().finish()
                    }
                    is SignUpState.Error -> {
                        viewBinding.textFieldCode.run {
                            setDescriptionText("")
                            setEmptyUIOfTextField()
                        }
                        handleSignUpErrorCode(state)
                    }
                    else -> {
                        viewBinding.textFieldCode.run {
                            setDescriptionText("")
                            setEmptyUIOfTextField()
                        }
                    }
                }
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
        ViewCompat.setWindowInsetsAnimationCallback(
            viewBinding.layoutButton,
            TranslateDeferringInsetsAnimationCallback(
                view = viewBinding.layoutButton,
                persistentInsetTypes = WindowInsetsCompat.Type.systemBars(),
                deferredInsetTypes = WindowInsetsCompat.Type.ime()
            )
        )
        viewBinding.btnSignUp.setOnButtonDebounceClickListener(this) {
            viewModel.requestInvalidSignUpCode()
        }
    }

    private fun handleSignUpErrorCode(error: SignUpState.Error) {
        val codeMessage = when (error.code) {
            MEMBER_INVALID_INVITE -> {
                "초대 코드를 다시 확인해주세요."
            }
            INVALID_PLATFORM_NAME -> {
                "잘못된 플랫폼 이름입니다."
            }
            else -> {
                "잠시 후 다시 시도해주세요."
            }
        }
        Toast.makeText(requireContext(), error.message ?: codeMessage, Toast.LENGTH_LONG).show()
    }

    private fun setUiOfCodeState(codeState: CodeState) {
        with(viewBinding.textFieldCode) {
            when (codeState.validationCode) {
                Validation.SUCCESS -> {
                    setDescriptionText("")
                    setSuccessUiOfTextField()
                }
                else -> {
                    setDescriptionText("")
                    setEmptyUIOfTextField()
                }
            }
        }
        viewBinding.btnSignUp.setButtonEnabled(codeState.isValidationState)
    }
}