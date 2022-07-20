package com.mashup.ui.attendance.platform

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.activity.viewModels
import com.mashup.R
import com.mashup.base.BaseActivity
import com.mashup.compose.theme.MashUpTheme
import com.mashup.data.model.PlatformInfo
import com.mashup.databinding.ActivityPlatformAttendanceBinding
import com.mashup.ui.attendance.crew.CrewAttendanceActivity
import com.mashup.ui.attendance.platform.PlatformAttendanceViewModel.Companion.EXTRA_SCHEDULE_ID
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlatformAttendanceActivity : BaseActivity<ActivityPlatformAttendanceBinding>() {
    private val viewModel: PlatformAttendanceViewModel by viewModels()

    override fun initViews() {
        super.initViews()
        viewBinding.viewCompose.setContent {
            MashUpTheme {
                when (val state = viewModel.platformAttendanceState.value) {
                    is PlatformAttendanceState.Success -> {
                        PlatformList(
                            totalAttendanceResponse = state.data,
                            onClickPlatform = ::moveToCrewAttendance
                        )
                    }
                }
            }
        }
    }

    private fun moveToCrewAttendance(platform: PlatformInfo) {
        val scheduleId = viewModel.scheduleId
        if (scheduleId == null) {
            Toast.makeText(this, "정보를 찾을 수 없습니다.", Toast.LENGTH_LONG).show()
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
                putExtra(EXTRA_SCHEDULE_ID, scheduleId)
            }
    }
}