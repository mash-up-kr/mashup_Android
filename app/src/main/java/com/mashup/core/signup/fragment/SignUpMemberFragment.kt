package com.mashup.core.signup.fragment

import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.mashup.R
import com.mashup.base.BaseFragment
import com.mashup.core.common.extensions.setEmptyUIOfTextField
import com.mashup.core.common.extensions.setFailedUiOfTextField
import com.mashup.core.common.extensions.setSuccessUiOfTextField
import com.mashup.core.common.model.Validation
import com.mashup.core.common.utils.keyboard.TranslateDeferringInsetsAnimationCallback
import com.mashup.core.model.Platform
import com.mashup.databinding.FragmentSignUpMemberBinding
import com.mashup.core.signup.MemberState
import com.mashup.core.signup.SignUpViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class SignUpMemberFragment : BaseFragment<FragmentSignUpMemberBinding>() {

    private val viewModel: SignUpViewModel by activityViewModels()

    override val layoutId: Int
        get() = R.layout.fragment_sign_up_member

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
            memberState.collectLatest { memberState ->
                setUiOfMemberState(memberState)
            }
        }
    }

    private fun initTextField() {
        viewBinding.textFieldName.addOnTextChangedListener {
            viewModel.setUserName(it)
        }
        viewBinding.textFieldName.setFocus()

        viewBinding.textFieldPlatform.setSelectionThrottleFirstClickListener(viewLifecycleOwner) {
            viewBinding.textFieldName.clearTextFieldFocus()
            findNavController().navigate(R.id.action_signUpMemberFragment_to_platFormSelectionDialog)
        }
    }

    private fun setUiOfMemberState(memberState: MemberState) = with(viewBinding) {
        setUiOfNameState(memberState)
        if (memberState.platform != Platform.UNKNOWN.detailName) {
            textFieldPlatform.setText(memberState.platform)
        }
        btnSignUp.setButtonEnabled(memberState.isValidationState)
    }

    private fun setUiOfNameState(memberState: MemberState) = with(viewBinding) {
        viewBinding.textFieldName.setText(memberState.name)
        when (memberState.isValidationName) {
            Validation.EMPTY -> {
                textFieldName.setEmptyUIOfTextField()
            }
            Validation.SUCCESS -> {
                textFieldName.setSuccessUiOfTextField()
            }
            Validation.FAILED -> {
                textFieldName.setFailedUiOfTextField()
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
            findNavController().navigate(R.id.action_signUpMemberFragment_to_termsAgreementDialog)
        }
    }
}
