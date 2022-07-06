package com.mashup.ui.attendance.crew

import android.content.Context
import android.content.Intent
import androidx.activity.viewModels
import com.mashup.R
import com.mashup.base.BaseActivity
import com.mashup.compose.theme.MashUpTheme
import com.mashup.databinding.ActivityCrewAttendanceBinding
import com.mashup.ui.attendance.crew.CrewAttendanceViewModel.Companion.EXTRA_PLATFORM_KEY
import com.mashup.ui.attendance.model.PlatformAttendance
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CrewAttendanceActivity : BaseActivity<ActivityCrewAttendanceBinding>() {
    private val viewModel: CrewAttendanceViewModel by viewModels()

    override fun initViews() {
        super.initViews()
        viewBinding.viewCompose.setContent {
            MashUpTheme {
                CrewList(
                    crewAttendanceList = viewModel.crewList.value
                )
            }
        }
    }

    override fun initObserves() {
        super.initObserves()
        viewModel.platformAttendance.observe(this) {
            viewBinding.toolbar.setTitle(
                "${it.platform.name}(${it.numberOfCrew})"
            )
        }
    }

    override val layoutId: Int
        get() = R.layout.activity_platform_attendance

    companion object {
        fun newIntent(context: Context, platformAttendance: PlatformAttendance) =
            Intent(context, CrewAttendanceActivity::class.java).apply {
                putExtra(EXTRA_PLATFORM_KEY, platformAttendance)
            }
    }
}