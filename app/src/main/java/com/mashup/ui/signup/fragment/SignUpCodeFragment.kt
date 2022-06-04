package com.mashup.ui.signup.fragment

import androidx.fragment.app.activityViewModels
import com.mashup.R
import com.mashup.base.BaseFragment
import com.mashup.databinding.FragmentSignUpCodeBinding
import com.mashup.ui.extensions.setFailedUiOfTextField
import com.mashup.ui.extensions.setSuccessUiOfTextField
import com.mashup.ui.signup.SignUpViewModel
import com.mashup.ui.signup.validationId
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpCodeFragment : BaseFragment<FragmentSignUpCodeBinding>() {

    private val viewModel: SignUpViewModel by activityViewModels()

    override val layoutId: Int
        get() = R.layout.fragment_sign_up_code

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