package com.mashup.ui.attendance.platform

import android.content.Context
import android.content.Intent
import androidx.activity.viewModels
import com.mashup.R
import com.mashup.base.BaseActivity
import com.mashup.common.model.NavigationAnimationType
import com.mashup.compose.theme.MashUpTheme
import com.mashup.constant.EXTRA_ANIMATION
import com.mashup.constant.EXTRA_SCHEDULE_ID
import com.mashup.data.model.PlatformInfo
import com.mashup.databinding.ActivityPlatformAttendanceBinding
import com.mashup.network.errorcode.EVENT_NOT_FOUND
import com.mashup.network.errorcode.SCHEDULE_NOT_FOUND
import com.mashup.ui.attendance.crew.CrewAttendanceActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlatformAttendanceActivity : BaseActivity<ActivityPlatformAttendanceBinding>() {
    private val viewModel: PlatformAttendanceViewModel by viewModels()

    override fun initViews() {
        super.initViews()
        initCompose()
        initButton()
    }

    private fun initCompose() {
        viewBinding.viewCompose.setContent {
            MashUpTheme {
                when (val state = viewModel.platformAttendanceState.value) {
                    PlatformAttendanceState.Loading -> {
                        showLoading()
                    }
                    is PlatformAttendanceState.Success -> {
                        hideLoading()
                        PlatformList(
                            notice = viewModel.notice.value,
                            totalAttendanceResponse = state.data,
                            isAttendingEvent = state.data.eventNum != 2 || !state.data.isEnd,
                            onClickPlatform = ::moveToCrewAttendance
                        )
                    }
                    is PlatformAttendanceState.Error -> {
                        hideLoading()
                        handleCommonError(state.code)
                        handlePlatformAttendanceErrorCode(state)
                    }
                    else -> {
                        hideLoading()
                    }
                }
            }
        }
    }

    private fun initButton() {
        viewBinding.toolbar.setOnBackButtonClickListener {
            finish()
        }
    }

    private fun handlePlatformAttendanceErrorCode(error: PlatformAttendanceState.Error) {
        val codeMessage = when (error.code) {
            SCHEDULE_NOT_FOUND -> {
                "스케줄 정보를 찾을 수 없습니다."
            }
            EVENT_NOT_FOUND -> {
                "스케줄 상세 정보를 찾을 수 없습니다."
            }
            else -> {
                null
            }
        }
        codeMessage?.run { showToast(codeMessage) }
    }

    private fun moveToCrewAttendance(platform: PlatformInfo) {
        val scheduleId = viewModel.scheduleId
        if (scheduleId == null) {
            showToast("스케줄 정보를 찾을 수 없습니다.")
            finish()
        } else {
            startActivity(
                CrewAttendanceActivity.newIntent(this, platform, scheduleId)
            )
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getPlatformAttendanceList()
    }

    override val layoutId: Int
        get() = R.layout.activity_platform_attendance

    companion object {
        fun newIntent(context: Context, scheduleId: Int) =
            Intent(context, PlatformAttendanceActivity::class.java).apply {
                putExtra(EXTRA_ANIMATION, NavigationAnimationType.PULL)
                putExtra(EXTRA_SCHEDULE_ID, scheduleId)
            }
    }
}