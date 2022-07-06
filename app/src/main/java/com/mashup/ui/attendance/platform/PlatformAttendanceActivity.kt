package com.mashup.ui.attendance.platform

import android.content.Context
import android.content.Intent
import androidx.activity.viewModels
import com.mashup.R
import com.mashup.base.BaseActivity
import com.mashup.compose.theme.MashUpTheme
import com.mashup.databinding.ActivityPlatformAttendanceBinding
import com.mashup.ui.attendance.crew.CrewAttendanceActivity
import com.mashup.ui.attendance.model.PlatformAttendance
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlatformAttendanceActivity : BaseActivity<ActivityPlatformAttendanceBinding>() {
    private val viewModel: PlatformAttendanceViewModel by viewModels()

    override fun initViews() {
        super.initViews()
        viewBinding.viewCompose.setContent {
            MashUpTheme {
                PlatformList(
                    notice = viewModel.notice.value,
                    platformAttendanceList = viewModel.platformList.value,
                    onClickPlatform = ::moveToCrewAttendance
                )
            }
        }
    }

    private fun moveToCrewAttendance(platformAttendance: PlatformAttendance) {
        startActivity(
            CrewAttendanceActivity.newIntent(this, platformAttendance)
        )
    }

    override val layoutId: Int
        get() = R.layout.activity_platform_attendance

    companion object {
        fun newIntent(context: Context) = Intent(context, PlatformAttendanceActivity::class.java)
    }
}