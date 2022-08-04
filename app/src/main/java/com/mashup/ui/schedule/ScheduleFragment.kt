package com.mashup.ui.schedule

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.mashup.R
import com.mashup.base.BaseFragment
import com.mashup.databinding.FragmentScheduleBinding
import com.mashup.extensions.onThrottleFirstClick
import com.mashup.ui.attendance.platform.PlatformAttendanceActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class ScheduleFragment : BaseFragment<FragmentScheduleBinding>() {

    private val viewModel: ScheduleViewModel by viewModels()

    override val layoutId: Int = R.layout.fragment_schedule

    private val scheduleAdapter by lazy {
        ScheduleViewPagerAdapter(
            object : OnItemClickListener {
                override fun onClickAttendanceList() {
                    startActivity(ScheduleDetailActivity.newIntent(requireContext()))
                }

                override fun onClickCrewAttendanceActivity(scheduleId: Int) {
                    startActivity(
                        PlatformAttendanceActivity.newIntent(
                            requireContext(),
                            scheduleId
                        )
                    )
                }
            }
        )
    }

    override fun initViews() {
        super.initViews()
        setUi()
    }

    private fun setUi() {
        setUiOfRefreshButton()
        setUiOfViewPager()
    }

    private fun setUiOfRefreshButton() {
        viewBinding.btnRefresh.onThrottleFirstClick(viewLifecycleOwner.lifecycleScope) {
            viewModel.getScheduleList()
        }
    }

    private fun setUiOfViewPager() {
        viewBinding.vpSchedule.apply {
            val pageMarginPx = resources.getDimensionPixelOffset(R.dimen.pageMargin)
            val pagerWidth = resources.getDimensionPixelOffset(R.dimen.pagerWidth)
            setPageTransformer { page, position ->
                page.translationX = ((position) * -(pageMarginPx + pagerWidth))
                page.scaleY = 1 - (0.1f * kotlin.math.abs(position))
            }
            adapter = scheduleAdapter
            offscreenPageLimit = 4
        }
    }

    override fun initObserves() {
        flowViewLifecycleScope {
            viewModel.scheduleState.collectLatest { state ->
                when (state) {
                    ScheduleState.Loading -> {
                        showLoading()
                    }
                    is ScheduleState.Success -> {
                        hideLoading()
                        setUiOfScheduleTitle(state.scheduleTitleState)
                    }
                    is ScheduleState.Error -> {
                        hideLoading()
                        handleCommonError(state.code)
                    }
                    else -> {
                        hideLoading()
                    }
                }
            }
        }
    }

    private fun setUiOfScheduleTitle(scheduleTitleState: ScheduleTitleState) {
        viewBinding.tvTitle.text = when (scheduleTitleState) {
            ScheduleTitleState.Empty -> {
                getString(R.string.empty_schedule)
            }
            is ScheduleTitleState.End -> {
                getString(R.string.end_schedule, scheduleTitleState.generatedNumber)
            }
            is ScheduleTitleState.DateCount -> {
                getString(R.string.event_list_title, scheduleTitleState.dataCount)
            }
        }
    }

    companion object {
        fun newInstance() = ScheduleFragment()
    }
}