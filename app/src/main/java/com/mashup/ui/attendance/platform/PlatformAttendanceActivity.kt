package com.mashup.ui.attendance.platform

import androidx.activity.viewModels
import com.mashup.R
import com.mashup.base.BaseActivity
import com.mashup.compose.theme.MashUpTheme
import com.mashup.databinding.ActivityPlatformAttendanceBinding
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
                    platformAttendanceList = viewModel.platformList.value
                )
            }
        }
    }

    override val layoutId: Int
        get() = R.layout.activity_platform_attendance
}