package com.mashup.ui.attendance.crew

import android.content.Context
import android.content.Intent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import com.mashup.R
import com.mashup.base.BaseActivity
import com.mashup.common.extensions.setStatusBarColorRes
import com.mashup.common.model.NavigationAnimationType
import com.mashup.compose.theme.MashUpTheme
import com.mashup.constant.EXTRA_ANIMATION
import com.mashup.data.model.PlatformInfo
import com.mashup.databinding.ActivityCrewAttendanceBinding
import com.mashup.network.errorcode.SCHEDULE_NOT_FOUND
import com.mashup.ui.attendance.crew.CrewAttendanceViewModel.Companion.EXTRA_PLATFORM_KEY
import com.mashup.ui.attendance.crew.CrewAttendanceViewModel.Companion.EXTRA_SCHEDULE_ID
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class CrewAttendanceActivity : BaseActivity<ActivityCrewAttendanceBinding>() {
    private val viewModel: CrewAttendanceViewModel by viewModels()

    override fun initViews() {
        super.initViews()

        setStatusBarColorRes(R.color.white)
        initButton()
        initCompose()
    }

    private fun initButton() {
        viewBinding.toolbar.setOnBackButtonClickListener {
            finish()
        }
    }

    private fun initCompose() {
        viewBinding.viewCompose.setContent {
            MashUpTheme {
                val crewState = viewModel.crewAttendanceState.collectAsState(
                    CrewAttendanceState.Empty
                )
                CrewList(
                    crewAttendanceList =
                    (crewState.value as? CrewAttendanceState.Success)?.data?.members ?: emptyList()
                )
            }
        }
    }

    override fun initObserves() {
        super.initObserves()
        viewModel.platformAttendance.observe(this) {
            viewBinding.toolbar.setTitle(
                "${it.platform.detailName}(${it.totalCount}명)"
            )
        }
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

    override val layoutId: Int
        get() = R.layout.activity_crew_attendance

    companion object {
        fun newIntent(
            context: Context,
            platformInfo: PlatformInfo,
            scheduleId: Int
        ) = Intent(context, CrewAttendanceActivity::class.java).apply {
            putExtra(EXTRA_ANIMATION, NavigationAnimationType.PULL)
            putExtra(EXTRA_PLATFORM_KEY, platformInfo)
            putExtra(EXTRA_SCHEDULE_ID, scheduleId)
        }
    }
}
