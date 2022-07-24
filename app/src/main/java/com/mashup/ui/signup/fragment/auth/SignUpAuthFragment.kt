package com.mashup.ui.signup.fragment.auth

import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.mashup.R
import com.mashup.base.BaseFragment
import com.mashup.databinding.FragmentSignUpAuthBinding
import com.mashup.extensions.scrollToTarget
import com.mashup.network.errorcode.MEMBER_DUPLICATED_IDENTIFICATION
import com.mashup.ui.extensions.setEmptyUIOfTextField
import com.mashup.ui.extensions.setFailedUiOfTextField
import com.mashup.ui.extensions.setSuccessUiOfTextField
import com.mashup.ui.signup.SignUpViewModel
import com.mashup.utils.keyboard.TranslateDeferringInsetsAnimationCallback
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class SignUpAuthFragment : BaseFragment<FragmentSignUpAuthBinding>() {

    private val authViewModel: SignUpAuthViewModel by viewModels()
    private val activityViewModel: SignUpViewModel by activityViewModels()

    override val layoutId: Int
        get() = R.layout.fragment_sign_up_auth

    override fun initViews() {
        initTextField()
        initButton()
    }

    override fun initObserves() {
        flowViewLifecycleScope {
            authViewModel.moveToNextScreen.collectLatest {
                findNavController().navigate(R.id.action_signUpAuthFragment_to_signUpMemberFragment)
            }
        }

        flowViewLifecycleScope {
            authViewModel.authScreenState.collectLatest { authScreenState ->
                setUiOfIdState(authScreenState.idState)
                setUiOfButtonState(authScreenState.buttonState)

                val validId = (authScreenState.idState as? SignUpIdState.Success)?.validId == true
                viewBinding.textFieldPwd.isVisible = validId
                viewBinding.textFieldPwdCheck.isVisible = validId

                if (validId) {
                    setUiOfPwdState(authScreenState.pwdState)
                    setUiOfPwdCheckState(authScreenState.pwdState, authScreenState.pwdCheckState)
                }
            }
        }
    }

    private fun initTextField() {
        viewBinding.textFieldId.run {
            addOnTextChangedListener { text ->
                authViewModel.setId(text)
                activityViewModel.setId(text)
            }
            setOnFocusChangedListener { hasFocus ->
                if (hasFocus) {
                    post {
                        viewBinding.scrollView.scrollToTarget(
                            viewBinding.layoutContent, this
                        )
                    }
                }
            }
        }

        viewBinding.textFieldPwd.run {
            addOnTextChangedListener { text ->
                authViewModel.setPwd(text)
                activityViewModel.setPwd(text)
            }
            setOnFocusChangedListener { hasFocus ->
                if (hasFocus) {
                    post {
                        viewBinding.scrollView.scrollToTarget(
                            viewBinding.layoutContent, this
                        )
                    }
                }
            }
        }
        viewBinding.textFieldPwdCheck.run {
            addOnTextChangedListener { text ->
                authViewModel.setPwdCheck(text)
                activityViewModel.setPwdCheck(text)
            }
            setOnFocusChangedListener { hasFocus ->
                if (hasFocus) {
                    post {
                        viewBinding.scrollView.scrollToTarget(
                            viewBinding.layoutContent, this
                        )
                    }
                }
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
        viewBinding.btnSignUp.setOnButtonClickListener {
            authViewModel.onClickNextButton()
        }
    }

    private fun setUiOfIdState(idState: SignUpIdState) = with(viewBinding) {
        when (idState) {
            SignUpIdState.Empty -> {
                textFieldId.setDescriptionText("")
                textFieldId.setEmptyUIOfTextField()
            }
            is SignUpIdState.Success -> {
                textFieldId.setDescriptionText("")
                textFieldId.setSuccessUiOfTextField()
            }
            is SignUpIdState.Error -> {
                val errorMessage = when (idState.code) {
                    MEMBER_DUPLICATED_IDENTIFICATION -> {
                        "이미 사용 중인 아이디에요."
                    }
                    else -> {
                        "영문 대소문자만 사용하여 15자 이내로 입력해 주세요."
                    }
                }
                textFieldId.setDescriptionText(idState.message ?: errorMessage)
                textFieldId.setFailedUiOfTextField()
            }
        }
    }

    private fun setUiOfButtonState(buttonState: SignUpButtonState) = with(viewBinding) {
        when (buttonState) {
            SignUpButtonState.Loading -> {
                btnSignUp.showLoading()
            }
            SignUpButtonState.Disable -> {
                btnSignUp.hideLoading()
                btnSignUp.setButtonEnabled(false)
            }
            else -> {
                btnSignUp.hideLoading()
                btnSignUp.setButtonEnabled(true)
            }
        }
    }

    private fun setUiOfPwdState(pwdState: SignUpPwdState) = with(viewBinding) {
        when (pwdState) {
            SignUpPwdState.Success -> {
                textFieldPwd.setDescriptionText("")
                textFieldPwd.setSuccessUiOfTextField()
            }
            SignUpPwdState.Error -> {
                textFieldPwd.setDescriptionText("영문, 숫자를 조합하여 8자 이상으로 입력해주세요.")
                textFieldPwd.setFailedUiOfTextField()
            }
            SignUpPwdState.Empty -> {
                textFieldPwd.setDescriptionText("")
                textFieldPwd.setEmptyUIOfTextField()
            }
        }
    }

    private fun setUiOfPwdCheckState(pwdState: SignUpPwdState, pwdCheckState: SignUpPwdCheckState) =
        with(viewBinding) {
            textFieldPwdCheck.setEnabledTextField(pwdState == SignUpPwdState.Success)
            when (pwdCheckState) {
                SignUpPwdCheckState.Success -> {
                    textFieldPwdCheck.setDescriptionText("")
                    textFieldPwdCheck.setSuccessUiOfTextField()
                }
                SignUpPwdCheckState.Error -> {
                    textFieldPwdCheck.setDescriptionText("비밀번호가 일치하지 않아요")
                    textFieldPwdCheck.setFailedUiOfTextField()
                }
                SignUpPwdCheckState.Empty -> {
                    textFieldPwdCheck.setDescriptionText("")
                    textFieldPwdCheck.setEmptyUIOfTextField()
                }
            }
        }
}