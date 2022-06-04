package com.mashup.ui.signup.fragment

import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.mashup.R
import com.mashup.base.BaseFragment
import com.mashup.databinding.FragmentSignUpAuthBinding
import com.mashup.databinding.FragmentSignUpMemberBinding
import com.mashup.ui.extensions.setFailedUiOfTextField
import com.mashup.ui.extensions.setSuccessUiOfTextField
import com.mashup.ui.signup.SignUpViewModel
import com.mashup.ui.signup.validationId
import com.mashup.ui.signup.validationPwd
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpAuthFragment : BaseFragment<FragmentSignUpAuthBinding>() {

    private val viewModel: SignUpViewModel by activityViewModels()

    override val layoutId: Int
        get() = R.layout.fragment_sign_up_auth

    override fun initViews() {
        initTextField()
        initButton()
    }

    private fun initTextField() {
        viewBinding.textFieldId.run {
            addOnTextChangedListener { text ->
                if (validationId(text)) {
                    setSuccessUiOfTextField()
                } else {
                    setFailedUiOfTextField()
                }
            }
        }

        viewBinding.textFieldPwd.run {
            addOnTextChangedListener { text ->
                if (validationPwd(text)) {
                    setSuccessUiOfTextField()
                } else {
                    setFailedUiOfTextField()
                }
            }
        }
    }

    private fun initButton() {
        viewBinding.btnSignUp.setOnButtonClickListener {
            findNavController().navigate(R.id.action_signUpAuthFragment_to_signUpMemberFragment)
        }
    }
}