package com.mashup.core.schedule

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.mashup.R
import com.mashup.base.BaseFragment
import com.mashup.common.extensions.fromHtml
import com.mashup.common.extensions.gone
import com.mashup.common.extensions.onThrottleFirstClick
import com.mashup.common.extensions.visible
import com.mashup.databinding.FragmentScheduleBinding
import com.mashup.core.attendance.platform.PlatformAttendanceActivity
import com.mashup.core.schedule.adapter.OnItemClickListener
import com.mashup.core.schedule.adapter.ScheduleViewPagerAdapter
import com.mashup.core.schedule.detail.ScheduleDetailActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce

@AndroidEntryPoint
class ScheduleFragment : BaseFragment<FragmentScheduleBinding>() {

    private val viewModel: ScheduleViewModel by viewModels()

    override val layoutId: Int = R.layout.fragment_schedule

    private val scheduleAdapter by lazy {
        ScheduleViewPagerAdapter(
            object : OnItemClickListener {
                override fun onClickEmptySchedule() {
                    showToast("볼 수 있는 일정이 없어요..!")
                }

                override fun onClickAttendanceList(scheduleId: Int) {
                    startActivity(
                        ScheduleDetailActivity.newIntent(
                            requireContext(),
                            scheduleId
                        )
                    )
                }

                override fun onClickCrewAttendanceActivity(scheduleId: Int) {
                    hideCoachMark()
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
        initSwipeRefreshLayout()
        initRefreshButton()
        initViewPager()
    }

    private fun initSwipeRefreshLayout() {
        viewBinding.layoutSwipe.apply {
            setOnRefreshListener {
                viewModel.getScheduleList()
                viewBinding.layoutSwipe.isRefreshing = false
            }
            setColorSchemeColors(
                ContextCompat.getColor(requireContext(), R.color.brand500)
            )
        }
    }

    private fun initRefreshButton() {
        viewBinding.btnRefresh.onThrottleFirstClick(viewLifecycleOwner.lifecycleScope) {
            viewModel.getScheduleList()
        }
    }

    private fun initViewPager() {
        viewBinding.vpSchedule.apply {
            val pageMarginPx = resources.getDimensionPixelOffset(R.dimen.pageMargin)
            val pagerWidth = resources.getDimensionPixelOffset(R.dimen.pagerWidth)
            setPageTransformer { page, position ->
                page.translationX = ((position) * -(pageMarginPx + pagerWidth))
                page.scaleY = 1 - (0.1f * kotlin.math.abs(position))
            }
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                    super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                    if (viewBinding.layoutCoachMark.root.visibility == View.VISIBLE) {
                        viewBinding.layoutCoachMark.root.gone()
                    }
                }

                override fun onPageScrollStateChanged(state: Int) {
                    super.onPageScrollStateChanged(state)
                    enableDisableSwipeRefresh(state == ViewPager.SCROLL_STATE_IDLE)
                }
            })
            adapter = scheduleAdapter
            offscreenPageLimit = 4
        }
    }

    override fun initObserves() {
        flowViewLifecycleScope {
            viewModel.scheduleState.collectLatest { state ->
                when (state) {
                    ScheduleState.Loading -> {
                        showRefreshSpinner()
                    }
                    is ScheduleState.Success -> {
                        hideRefreshSpinner()
                        setUiOfScheduleTitle(state.scheduleTitleState)
                        scheduleAdapter.submitList(state.scheduleList)
                        viewBinding.vpSchedule.currentItem = state.schedulePosition
                    }
                    is ScheduleState.Error -> {
                        hideRefreshSpinner()
                        handleCommonError(state.code)
                    }
                    else -> {
                        hideRefreshSpinner()
                    }
                }
            }
        }

        flowViewLifecycleScope {
            viewModel.showCoachMark
                .debounce(1000L)
                .collectLatest {
                    showCoachMark()
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
                getString(R.string.event_list_title, scheduleTitleState.dataCount).fromHtml()
            }
        }
    }

    private fun showCoachMark() = with(viewBinding.layoutCoachMark.root) {
        visible()
        alpha = 0.0f

        animate()
            .translationY(TRANSLATION_COACH_MARK_Y)
            .alpha(1.0f)

        postDelayed({
            if (isViewBindingExist()) {
                hideCoachMark()
            }
        }, 5000)
    }

    private fun hideCoachMark() = with(viewBinding.layoutCoachMark.root) {
        if (visibility == View.GONE) return@with
        animate()
            .translationY(0f)
            .alpha(0.0f)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    super.onAnimationEnd(animation)
                    gone()
                }
            })
    }

    override fun onResume() {
        super.onResume()
        viewModel.getScheduleList()
    }

    private fun showRefreshSpinner() {
        viewBinding.layoutSwipe.isRefreshing = true
    }

    private fun hideRefreshSpinner() {
        viewBinding.layoutSwipe.isRefreshing = false
    }

    companion object {
        private const val TRANSLATION_COACH_MARK_Y = 10f

        fun newInstance() = ScheduleFragment()
    }

    private fun enableDisableSwipeRefresh(enable: Boolean) {
        viewBinding.layoutSwipe.isEnabled = enable
    }
}
