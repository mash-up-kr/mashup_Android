package com.mashup.ui.schedule

import com.mashup.R
import com.mashup.base.BaseFragment
import com.mashup.databinding.FragmentScheduleBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScheduleFragment : BaseFragment<FragmentScheduleBinding>() {

    override val layoutId: Int = R.layout.fragment_schedule
}