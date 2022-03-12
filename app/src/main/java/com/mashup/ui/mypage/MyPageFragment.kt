package com.mashup.ui.mypage

import com.mashup.R
import com.mashup.base.BaseFragment
import com.mashup.databinding.FragmentMyPageBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyPageFragment : BaseFragment<FragmentMyPageBinding>() {

    companion object {
        fun newInstance() = MyPageFragment()
    }

    override val layoutId: Int = R.layout.fragment_my_page
}