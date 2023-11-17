package com.mashup.ui.attendance.crew

import android.content.Context
import android.content.Intent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.mashup.R
import com.mashup.base.BaseActivity
import com.mashup.constant.EXTRA_ANIMATION
import com.mashup.core.common.constant.SCHEDULE_NOT_FOUND
import com.mashup.core.common.extensions.setStatusBarColorRes
import com.mashup.core.common.model.NavigationAnimationType
import com.mashup.core.ui.theme.MashUpTheme
import com.mashup.data.model.PlatformInfo
import com.mashup.databinding.ActivityCrewAttendanceBinding
import com.mashup.ui.attendance.crew.CrewAttendanceViewModel.Companion.EXTRA_PLATFORM_KEY
import com.mashup.ui.attendance.crew.CrewAttendanceViewModel.Companion.EXTRA_SCHEDULE_ID
import com.mashup.ui.attendance.member.MemberInfoDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class CrewAttendanceActivity : BaseActivity<ActivityCrewAttendanceBinding>() {
    private val viewModel: CrewAttendanceViewModel by viewModels()

    override fun initViews() {
        super.initViews()
        setStatusBarColorRes(com.mashup.core.common.R.color.white)
        initCompose()
    }

    private fun initCompose() {
        viewBinding.viewCompose.setContent {
            MashUpTheme {
                val crewState by viewModel.crewAttendanceState.collectAsState(
                    CrewAttendanceState.Empty
                )
                CrewScreen(
                    modifier = Modifier.fillMaxSize(),
                    crewAttendanceState = crewState,
                    onClickBackButton = ::finish,
                    showMemberInfoDialog = ::showMemberInfoDialog
                )
            }
        }
    }

    override fun initObserves() {
        super.initObserves()
        flowLifecycleScope {
            viewModel.crewAttendanceState.collectLatest { state ->
                when (state) {
                    CrewAttendanceState.Loading -> {
                        showLoading()
                    }
                    is CrewAttendanceState.Error -> {
                        hideLoading()
                        handleCommonError(state.code)
                        handleCrewAttendanceErrorCode(state)
                    }
                    else -> {
                        hideLoading()
                    }
                }
            }
        }
    }

    private fun handleCrewAttendanceErrorCode(error: CrewAttendanceState.Error) {
        val codeMessage = when (error.code) {
            SCHEDULE_NOT_FOUND -> {
                "스케줄 정보를 찾을 수 없습니다."
            }
            else -> {
                null
            }
        }
        codeMessage?.run { showToast(codeMessage) }
    }

    private fun showMemberInfoDialog() {
        MemberInfoDialog.newInstance().show(supportFragmentManager, "MemberInfoDialog")
    }

    override val layoutId: Int
        get() = R.layout.activity_crew_attendance

    companion object {
        fun newIntent(
            context: Context,
            platformInfo: PlatformInfo,
            scheduleId: Int
        ) = Intent(context, CrewAttendanceActivity::class.java).apply {
            putExtra(EXTRA_ANIMATION, NavigationAnimationType.SLIDE)
            putExtra(EXTRA_PLATFORM_KEY, platformInfo)
            putExtra(EXTRA_SCHEDULE_ID, scheduleId)
        }
    }
}
