package com.mashup.ui.register.ui.signin.fragment

import com.mashup.R
import com.mashup.base.BaseFragment
import com.mashup.databinding.FragmentSignInMemberInfoBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInMemberInfoFragment : BaseFragment<FragmentSignInMemberInfoBinding>() {

    private lateinit var viewModel: SignInViewModel

    override val layoutId: Int
        get() = R.layout.fragment_sign_in_member_info

}