package com.mashup.ui.signup.fragment

import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.mashup.R
import com.mashup.base.BaseFragment
import com.mashup.databinding.FragmentSignUpMemberBinding
import com.mashup.ui.signup.SignUpViewModel
import com.mashup.ui.signup.state.MemberState
import com.mashup.utils.keyboard.TranslateDeferringInsetsAnimationCallback
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class SignUpMemberFragment : BaseFragment<FragmentSignUpMemberBinding>() {

    private val viewModel: SignUpViewModel by activityViewModels()

    override val layoutId: Int
        get() = R.layout.fragment_sign_up_member

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

        viewBinding.textFieldPlatform.setSelectionThrottleFirstClickListener(viewLifecycleOwner) {
            viewBinding.textFieldName.clearTextFieldFocus()
            findNavController().navigate(R.id.action_signUpMemberFragment_to_platFormSelectionDialog)
        }
    }

    private fun setUiOfMemberState(memberState: MemberState) = with(viewBinding) {
        textFieldPlatform.setText(memberState.platform)
        btnSignUp.setButtonEnabled(memberState.isValidationState)
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