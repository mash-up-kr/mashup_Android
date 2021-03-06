package com.mashup.ui.signup.fragment

import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.mashup.R
import com.mashup.base.BaseFragment
import com.mashup.databinding.FragmentSignUpAuthBinding
import com.mashup.extensions.scrollToTarget
import com.mashup.ui.extensions.setEmptyUIOfTextField
import com.mashup.ui.extensions.setFailedUiOfTextField
import com.mashup.ui.extensions.setSuccessUiOfTextField
import com.mashup.ui.extensions.setValidation
import com.mashup.common.Validation
import com.mashup.ui.signup.SignUpViewModel
import com.mashup.ui.signup.state.AuthState
import com.mashup.utils.keyboard.TranslateDeferringInsetsAnimationCallback
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class SignUpAuthFragment : BaseFragment<FragmentSignUpAuthBinding>() {

    private val viewModel: SignUpViewModel by activityViewModels()

    override val layoutId: Int
        get() = R.layout.fragment_sign_up_auth

    override fun initViews() {
        initTextField()
        initButton()
    }

    override fun initObserves() = with(viewModel) {
        flowViewLifecycleScope {
            authState.collectLatest {
                setUiOfAuthState(it)
            }
        }
    }

    private fun initTextField() {
        viewBinding.textFieldId.run {
            addOnTextChangedListener { text ->
                viewModel.setId(text)
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
                viewModel.setPwd(text)
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
                viewModel.setPwdCheck(text)
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
            findNavController().navigate(R.id.action_signUpAuthFragment_to_signUpMemberFragment)
        }
    }

    private fun setUiOfAuthState(authState: AuthState) = with(viewBinding) {
        textFieldId.setValidation(authState.validationId)
        textFieldPwd.setValidation(authState.validationPwd)

        textFieldPwdCheck.setEnabledTextField(authState.validationPwd == Validation.SUCCESS)
        when (authState.validationPwdCheck) {
            Validation.SUCCESS -> {
                textFieldPwdCheck.setDescriptionText("")
                textFieldPwdCheck.setSuccessUiOfTextField()
            }
            Validation.FAILED -> {
                textFieldPwdCheck.setDescriptionText("??????????????? ???????????? ?????????")
                textFieldPwdCheck.setFailedUiOfTextField()
            }
            Validation.EMPTY -> {
                textFieldPwdCheck.setDescriptionText("")
                textFieldPwdCheck.setEmptyUIOfTextField()
            }
        }

        btnSignUp.setButtonEnabled(authState.isValidationState)
    }
}