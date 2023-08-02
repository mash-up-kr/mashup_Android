package com.mashup.ui.attendance.platform

import android.content.Context
import android.content.Intent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.mashup.R
import com.mashup.base.BaseActivity
import com.mashup.constant.EXTRA_ANIMATION
import com.mashup.constant.EXTRA_SCHEDULE_ID
import com.mashup.core.common.constant.EVENT_NOT_FOUND
import com.mashup.core.common.constant.SCHEDULE_NOT_FOUND
import com.mashup.core.common.model.NavigationAnimationType
import com.mashup.core.ui.theme.MashUpTheme
import com.mashup.data.model.PlatformInfo
import com.mashup.databinding.ActivityPlatformAttendanceBinding
import com.mashup.ui.attendance.crew.CrewAttendanceActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlatformAttendanceActivity : BaseActivity<ActivityPlatformAttendanceBinding>() {
    private val viewModel: PlatformAttendanceViewModel by viewModels()

    override fun initViews() {
        super.initViews()
        initCompose()
    }

    private fun initCompose() {
        viewBinding.viewCompose.setContent {
            MashUpTheme {
                val state = viewModel.platformAttendanceState.value
                when (state) {
                    PlatformAttendanceState.Loading -> {
                        showLoading()
                    }
                    is PlatformAttendanceState.Success -> {
                        hideLoading()
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
                PlatformAttendanceScreen(
                    modifier = Modifier.fillMaxSize(),
                    platformAttendanceState = state,
                    onClickPlatform = ::moveToCrewAttendance,
                    onClickBackButton = {
                        finish()
                    }
                )
            }
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
                putExtra(EXTRA_ANIMATION, NavigationAnimationType.SLIDE)
                putExtra(EXTRA_SCHEDULE_ID, scheduleId)
            }
    }
}
