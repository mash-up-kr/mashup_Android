package com.mashup.ui.signup.fragment

import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.mashup.R
import com.mashup.base.BaseFragment
import com.mashup.databinding.FragmentSignUpMemberBinding
import com.mashup.ui.signup.SignUpViewModel
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
        flowLifecycleScope {
            platform.collectLatest {
                viewBinding.textFieldPlatform.setText(it)
            }
        }
    }

    private fun initTextField() {
        viewBinding.textFieldName.addOnTextChangedListener {
            viewModel.setUserName(it)
        }

        viewBinding.textFieldPlatform.setSelectionClickListener {
            findNavController().navigate(R.id.action_signUpMemberFragment_to_platFormSelectionDialog)
        }
    }

    private fun initButton() {
        viewBinding.btnSignUp.setOnButtonClickListener {
            findNavController().navigate(R.id.action_signUpMemberFragment_to_signUpCodeFragment)
        }
    }
}