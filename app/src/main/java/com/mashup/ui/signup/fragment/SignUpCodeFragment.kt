package com.mashup.ui.signup.fragment

import android.content.Intent
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.activityViewModels
import com.mashup.R
import com.mashup.base.BaseFragment
import com.mashup.common.extensions.setEmptyUIOfTextField
import com.mashup.common.extensions.setFailedUiOfTextField
import com.mashup.common.extensions.setSuccessUiOfTextField
import com.mashup.common.model.Validation
import com.mashup.common.utils.keyboard.TranslateDeferringInsetsAnimationCallback
import com.mashup.databinding.FragmentSignUpCodeBinding
import com.mashup.network.errorcode.ATTENDANCE_CODE_DUPLICATED
import com.mashup.network.errorcode.INVALID_PLATFORM_NAME
import com.mashup.network.errorcode.MEMBER_INVALID_INVITE
import com.mashup.ui.main.MainActivity
import com.mashup.ui.signup.SignUpState
import com.mashup.ui.signup.SignUpViewModel
import com.mashup.ui.signup.state.CodeState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class SignUpCodeFragment : BaseFragment<FragmentSignUpCodeBinding>() {

    private val viewModel: SignUpViewModel by activityViewModels()

    override val layoutId: Int
        get() = R.layout.fragment_sign_up_code

    override fun onResume() {
        super.onResume()
        viewModel.setToolbarDividerVisible(false)
        viewModel.setToolbarCloseVisible(true)
    }

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
                    SignUpState.Loading -> {
                        viewBinding.btnSignUp.showLoading()
                    }
                    SignUpState.InvalidCode -> {
                        viewBinding.btnSignUp.hideLoading()
                        viewBinding.textFieldCode.run {
                            setDescriptionText("가입코드가 일치하지 않아요.")
                            setFailedUiOfTextField()
                        }
                    }
                    SignUpState.Success -> {
                        viewBinding.btnSignUp.hideLoading()
                        requireActivity().run {
                            showToast("회원가입 성공했습니다!")
                            startActivity(
                                Intent(requireContext(), MainActivity::class.java).apply {
                                    flags =
                                        Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                }
                            )
                            finish()
                        }
                    }
                    is SignUpState.Error -> {
                        viewBinding.btnSignUp.hideLoading()
                        viewBinding.textFieldCode.run {
                            setDescriptionText("")
                            setEmptyUIOfTextField()
                        }
                        handleCommonError(state.code)
                        handleSignUpErrorCode(state)
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
        viewBinding.textFieldCode.setFocus()
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
        viewBinding.btnSignUp.setOnButtonThrottleFirstClickListener(this) {
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
            ATTENDANCE_CODE_DUPLICATED -> {
                "코드 "
            }
            else -> {
                null
            }
        }
        codeMessage?.run { showToast(this) }
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