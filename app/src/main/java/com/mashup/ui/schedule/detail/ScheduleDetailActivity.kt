package com.mashup.ui.schedule.detail

import android.content.Context
import android.content.Intent
import androidx.activity.viewModels
import com.mashup.R
import com.mashup.base.BaseActivity
import com.mashup.databinding.ActivityScheduleDetailBinding
import com.mashup.ui.constant.EXTRA_SCHEDULE_ID
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class ScheduleDetailActivity : BaseActivity<ActivityScheduleDetailBinding>() {
    override val layoutId = R.layout.activity_schedule_detail

    private val viewModel: ScheduleDetailViewModel by viewModels()

    private val eventDetailAdapter by lazy {
        EventDetailAdapter()
    }

    override fun initViews() {
        initButton()
        viewBinding.rvEvent.apply {
            adapter = eventDetailAdapter
        }
    }

    override fun initObserves() {
        super.initObserves()
        flowLifecycleScope {
            viewModel.scheduleState.collectLatest { state ->
                when (state) {
                    ScheduleState.Loading -> {
                        showLoading()
                    }
                    is ScheduleState.Success -> {
                        hideLoading()
                        eventDetailAdapter.submitList(state.eventDetailList)
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

    private fun initButton() {
        viewBinding.btnReturn.setOnClickListener {
            onBackPressed()
        }
    }

    companion object {
        fun newIntent(context: Context, scheduleId: Int) =
            Intent(context, ScheduleDetailActivity::class.java).apply {
                putExtra(EXTRA_SCHEDULE_ID, scheduleId)
            }
    }

}
