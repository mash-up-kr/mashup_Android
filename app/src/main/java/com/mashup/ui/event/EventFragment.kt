package com.mashup.ui.event

import androidx.fragment.app.viewModels
import com.mashup.R
import com.mashup.base.BaseFragment
import com.mashup.databinding.FragmentMyPageBinding

class EventFragment : BaseFragment<FragmentMyPageBinding>() {

    private val viewModel: EventViewModel by viewModels()

    override val layoutId: Int = R.layout.fragment_event
    override fun initViews() {
        super.initViews()

    }

    override fun initObserves() {
    }

    companion object {
        fun newInstance() = EventFragment()
    }

}