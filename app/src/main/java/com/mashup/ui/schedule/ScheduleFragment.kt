package com.mashup.ui.schedule

import android.os.Bundle
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.mashup.R
import com.mashup.base.BaseFragment
import com.mashup.databinding.FragmentScheduleBinding
import com.mashup.ui.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScheduleFragment : BaseFragment<FragmentScheduleBinding>() {

    private val viewModel: ScheduleViewModel by viewModels()
    private val mainViewModel: MainViewModel by activityViewModels()

    override val layoutId: Int = R.layout.fragment_schedule

//    private val scheduleAdapter by lazy {
//        ScheduleViewPagerAdapter(
//            object : OnItemClickListener {
//                override fun onClickEmptySchedule() {
//                    showToast("볼 수 있는 일정이 없어요..!")
//                }
//
//                override fun onClickScheduleInformation(scheduleId: Int) {
//                    AnalyticsManager.addEvent(eventName = LOG_SCHEDULE_EVENT_DETAIL)
//                    startActivity(
//                        ScheduleDetailActivity.newIntent(
//                            requireContext(),
//                            scheduleId
//                        )
//                    )
//                }

//                override fun onClickAttendanceInfoButton(scheduleId: Int) {
//                    AnalyticsManager.addEvent(eventName = LOG_SCHEDULE_STATUS_CONFIRM)
//                    hideCoachMark()
//                    startActivity(
//                        PlatformAttendanceActivity.newIntent(
//                            requireContext(),
//                            scheduleId
//                        )
//                    )
//                }
//            },
//            enabledSwipeRefreshLayout = { enabled ->
//                viewBinding.layoutSwipe.isEnabled = enabled
//            }
//        )
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getScheduleList()

    }

    override fun initViews() {
        super.initViews()
        viewBinding.cvSchedule.setContent {
            ScheduleRoute(
                modifier = Modifier.fillMaxSize(),
                mainViewModel = mainViewModel,
                viewModel = viewModel
            )
        }
//        initSwipeRefreshLayout()
//        initViewPager()
//        initButtons()
    }

//    private fun initSwipeRefreshLayout() {
//        viewBinding.layoutSwipe.apply {
//            setOnRefreshListener {
//                viewModel.getScheduleList()
//                viewBinding.layoutSwipe.isRefreshing = false
//            }
//            setColorSchemeColors(
//                ContextCompat.getColor(requireContext(), com.mashup.core.common.R.color.brand500)
//            )
//        }
//    }
//
//    private fun initViewPager() {
//        viewBinding.vpSchedule.apply {
//            val pageMarginPx = resources.getDimensionPixelOffset(R.dimen.pageMargin)
//            val pagerWidth = resources.getDimensionPixelOffset(R.dimen.pagerWidth)
//            setPageTransformer { page, position ->
//                page.translationX = ((position) * -(pageMarginPx + pagerWidth))
//                page.scaleY = 1 - (0.1f * kotlin.math.abs(position))
//            }
//            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
//                override fun onPageScrolled(
//                    position: Int,
//                    positionOffset: Float,
//                    positionOffsetPixels: Int
//                ) {
//                    super.onPageScrolled(position, positionOffset, positionOffsetPixels)
//                    if (viewBinding.layoutCoachMark.root.visibility == View.VISIBLE) {
//                        viewBinding.layoutCoachMark.root.gone()
//                    }
//                }
//
//                override fun onPageScrollStateChanged(state: Int) {
//                    super.onPageScrollStateChanged(state)
//                    enableDisableSwipeRefresh(state == ViewPager.SCROLL_STATE_IDLE)
//                }
//            })
//            adapter = scheduleAdapter
//            offscreenPageLimit = 4
//        }
//    }
//
//    private fun initButtons() {
//        viewBinding.btnDanggnEntryPoint.onThrottleFirstClick(lifecycleScope) {
//            mainViewModel.disablePopup(MainPopupType.DANGGN)
//            startActivity(
//                ShakeDanggnActivity.newIntent(requireContext())
//            )
//        }
//    }
//
//    override fun initObserves() {
//        flowViewLifecycleScope {
//            viewModel.scheduleState.collectLatest { state ->
//                when (state) {
//                    ScheduleState.Loading -> {
//                        showRefreshSpinner()
//                    }
//                    is ScheduleState.Success -> {
//                        hideRefreshSpinner()
//                        setUiOfScheduleTitle(state.scheduleTitleState)
//                        scheduleAdapter.submitList(state.scheduleList)
//                        viewBinding.vpSchedule.currentItem = state.schedulePosition
//                    }
//                    is ScheduleState.Error -> {
//                        hideRefreshSpinner()
//                        handleCommonError(state.code)
//                    }
//                    else -> {
//                        hideRefreshSpinner()
//                    }
//                }
//            }
//        }
//
//        flowViewLifecycleScope {
//            viewModel.showCoachMark
//                .debounce(1000L)
//                .collectLatest {
//                    showCoachMark()
//                }
//        }
//
//        flowViewLifecycleScope {
//            mainViewModel.onAttendance.collectLatest {
//                viewModel.getScheduleList()
//            }
//        }
//    }
//
//    private fun setUiOfScheduleTitle(scheduleTitleState: ScheduleTitleState) {
//        viewBinding.tvTitle.text = when (scheduleTitleState) {
//            ScheduleTitleState.Empty -> {
//                getString(R.string.empty_schedule)
//            }
//            is ScheduleTitleState.End -> {
//                getString(R.string.end_schedule, scheduleTitleState.generatedNumber)
//            }
//            is ScheduleTitleState.DateCount -> {
//                getString(R.string.event_list_title, scheduleTitleState.dataCount).fromHtml()
//            }
//            is ScheduleTitleState.SchedulePreparing -> {
//                getString(R.string.preparing_attendance)
//            }
//        }
//    }
//
//    private fun showCoachMark() = with(viewBinding.layoutCoachMark.root) {
//        visible()
//        alpha = 0.0f
//
//        animate()
//            .translationY(TRANSLATION_COACH_MARK_Y)
//            .alpha(1.0f)
//
//        postDelayed({
//            if (isViewBindingExist()) {
//                hideCoachMark()
//            }
//        }, 5000)
//    }
//
//    private fun hideCoachMark() = with(viewBinding.layoutCoachMark.root) {
//        if (visibility == View.GONE) return@with
//        animate()
//            .translationY(0f)
//            .alpha(0.0f)
//            .setListener(object : AnimatorListenerAdapter() {
//                override fun onAnimationEnd(animation: Animator) {
//                    super.onAnimationEnd(animation)
//                    gone()
//                }
//            })
//    }
//
//    private fun showRefreshSpinner() {
//        viewBinding.layoutSwipe.isRefreshing = true
//    }
//
//    private fun hideRefreshSpinner() {
//        viewBinding.layoutSwipe.isRefreshing = false
//    }
//
    companion object {
        private const val TRANSLATION_COACH_MARK_Y = 10f

        fun newInstance() = ScheduleFragment()
    }

//    private fun enableDisableSwipeRefresh(enable: Boolean) {
//        viewBinding.layoutSwipe.isEnabled = enable
//    }
}
