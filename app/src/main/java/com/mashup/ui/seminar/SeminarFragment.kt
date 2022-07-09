package com.mashup.ui.seminar

import com.mashup.R
import com.mashup.base.BaseFragment
import com.mashup.databinding.FragmentScheduleBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SeminarFragment : BaseFragment<FragmentScheduleBinding>() {

    companion object {
        fun newInstance() = SeminarFragment()
    }

    override val layoutId: Int = R.layout.fragment_schedule
}