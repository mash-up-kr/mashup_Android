package com.mashup.ui.signin.fragment

import com.mashup.R
import com.mashup.base.BaseFragment
import com.mashup.databinding.FragmentSignInCodeBinding
import com.mashup.databinding.FragmentSignInMemberInfoBinding
import com.mashup.ui.extensions.setFailedUiOfTextField
import com.mashup.ui.extensions.setSuccessUiOfTextField
import com.mashup.ui.signin.SignInViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInCodeFragment : BaseFragment<FragmentSignInCodeBinding>() {

    private lateinit var viewModel: SignInViewModel

    override val layoutId: Int
        get() = R.layout.fragment_sign_in_code

    override fun initViews() {
        initTextField()
        initButton()
    }

    private fun initTextField() {
        viewBinding.textFieldCode.run {
            addOnTextChangedListener { text ->
                if (validationId(text)) {
                    setSuccessUiOfTextField()
                } else {
                    setFailedUiOfTextField()
                }
            }
        }
    }

    private fun initButton() {
        viewBinding.btnSignIn.setOnButtonClickListener {

        }
    }
}