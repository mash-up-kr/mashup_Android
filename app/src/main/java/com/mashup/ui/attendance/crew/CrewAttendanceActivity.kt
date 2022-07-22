package com.mashup.ui.attendance.crew

import android.content.Context
import android.content.Intent
import androidx.activity.viewModels
import com.mashup.R
import com.mashup.base.BaseActivity
import com.mashup.compose.theme.MashUpTheme
import com.mashup.data.model.PlatformInfo
import com.mashup.databinding.ActivityCrewAttendanceBinding
import com.mashup.ui.attendance.crew.CrewAttendanceViewModel.Companion.EXTRA_PLATFORM_KEY
import com.mashup.ui.attendance.crew.CrewAttendanceViewModel.Companion.EXTRA_SCHEDULE_ID
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CrewAttendanceActivity : BaseActivity<ActivityCrewAttendanceBinding>() {
    private val viewModel: CrewAttendanceViewModel by viewModels()

    override fun initViews() {
        super.initViews()
        viewBinding.viewCompose.setContent {
            MashUpTheme {
                when (val state = viewModel.crewAttendanceState.value) {
                    is CrewAttendanceState.Success -> {
                        CrewList(
                            crewAttendanceList = state.data.members
                        )
                    }
                }
            }
        }
    }

    override fun initObserves() {
        super.initObserves()
        viewModel.platformAttendance.observe(this) {
            viewBinding.toolbar.setTitle(
                "${it.platform.name}(${it.attendanceCount})"
            )
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getCrewAttendanceList()
    }

    override val layoutId: Int
        get() = R.layout.activity_platform_attendance

    companion object {
        fun newIntent(
            context: Context, platformInfo: PlatformInfo, scheduleId: Int
        ) = Intent(context, CrewAttendanceActivity::class.java).apply {
            putExtra(EXTRA_PLATFORM_KEY, platformInfo)
            putExtra(EXTRA_SCHEDULE_ID, scheduleId)
        }
    }
}