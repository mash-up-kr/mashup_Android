package com.mashup.ui.password.fragment.changepassword

import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.activityViewModels
import com.mashup.R
import com.mashup.base.BaseFragment
import com.mashup.core.common.extensions.scrollToTarget
import com.mashup.core.common.extensions.setEmptyUIOfTextField
import com.mashup.core.common.extensions.setFailedUiOfTextField
import com.mashup.core.common.extensions.setSuccessUiOfTextField
import com.mashup.core.common.utils.keyboard.TranslateDeferringInsetsAnimationCallback
import com.mashup.databinding.FragmentChangePasswordBinding
import com.mashup.ui.password.ButtonState
import com.mashup.ui.password.PasswordViewModel
import com.mashup.ui.password.PwdCheckState
import com.mashup.ui.password.PwdState
import com.mashup.ui.password.ScreenState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class ChangePasswordFragment : BaseFragment<FragmentChangePasswordBinding>() {

    private val passwordViewModel: PasswordViewModel by activityViewModels()
    override val layoutId: Int
        get() = R.layout.fragment_change_password

    override fun initViews() {
        initTextField()
        initButton()
    }

    override fun initObserves() {
        flowViewLifecycleScope {
            passwordViewModel.passwordScreenState.collectLatest { state ->
                setUiOfButtonState(state.buttonState)
                setUiOfPwdState(state.pwdState)
                setUiOfPwdCheckState(state.pwdState, state.pwdCheckState)
            }
        }

        flowViewLifecycleScope {
            passwordViewModel.moveToNextScreen.collectLatest {
                requireActivity().finish()
            }
        }
    }

    private fun initTextField() {
        viewBinding.textFieldPwd.apply {
            addOnTextChangedListener { text ->
                passwordViewModel.updatePassword(text)
            }
            setOnFocusChangedListener { hasFocus ->
                if (hasFocus) {
                    viewBinding.scrollView.scrollToTarget(
                        viewBinding.layoutContent,
                        this,
                    )
                }
            }
        }
        viewBinding.textFieldPwdCheck.apply {
            addOnTextChangedListener { text ->
                passwordViewModel.updatePasswordCheck(text)
            }
            setOnFocusChangedListener { hasFocus ->
                if (hasFocus) {
                    viewBinding.scrollView.scrollToTarget(
                        viewBinding.layoutContent,
                        this,
                    )
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
                deferredInsetTypes = WindowInsetsCompat.Type.ime(),
            ),
        )
        viewBinding.btnComplete.setOnButtonThrottleFirstClickListener(viewLifecycleOwner) {
            passwordViewModel.onClickNextButton(ScreenState.ChangePassword)
        }
    }

    private fun setUiOfPwdState(pwdState: PwdState) = with(viewBinding) {
        when (pwdState) {
            is PwdState.Success -> {
                textFieldPwd.setSuccessUiOfTextField()
            }
            is PwdState.Error -> {
                textFieldPwd.setFailedUiOfTextField()
            }
            is PwdState.Empty -> {
                textFieldPwd.setEmptyUIOfTextField()
            }
        }
    }

    private fun setUiOfPwdCheckState(pwdState: PwdState, pwdCheckState: PwdCheckState) =
        with(viewBinding) {
            textFieldPwdCheck.setEnabledTextField(pwdState is PwdState.Success)
            when (pwdCheckState) {
                is PwdCheckState.Success -> {
                    textFieldPwdCheck.setDescriptionText("")
                    textFieldPwdCheck.setSuccessUiOfTextField()
                }
                is PwdCheckState.Error -> {
                    textFieldPwdCheck.setDescriptionText("비밀번호가 일치하지 않아요.")
                    textFieldPwdCheck.setFailedUiOfTextField()
                }
                is PwdCheckState.Empty -> {
                    textFieldPwdCheck.setDescriptionText("")
                    textFieldPwdCheck.setEmptyUIOfTextField()
                }
            }
        }

    private fun setUiOfButtonState(buttonState: ButtonState) = with(viewBinding) {
        when (buttonState) {
            is ButtonState.Loading -> {
                btnComplete.showLoading()
            }
            is ButtonState.Disable -> {
                btnComplete.hideLoading()
                btnComplete.setButtonEnabled(false)
            }
            else -> {
                btnComplete.hideLoading()
                btnComplete.setButtonEnabled(true)
            }
        }
    }
}
