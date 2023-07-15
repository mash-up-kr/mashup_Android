package com.mashup.ui.danggn

import android.content.Context
import android.content.Intent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.mashup.R
import com.mashup.base.BaseActivity
import com.mashup.constant.EXTRA_ACTIVITY_ENTER_TYPE
import com.mashup.constant.EXTRA_ANIMATION
import com.mashup.constant.log.LOG_DANGGN
import com.mashup.constant.log.LOG_DANGGN_HELP
import com.mashup.core.common.model.ActivityEnterType
import com.mashup.core.common.model.NavigationAnimationType
import com.mashup.core.ui.theme.MashUpTheme
import com.mashup.databinding.ActivityShakeDanggnBinding
import com.mashup.feature.danggn.DanggnUiState
import com.mashup.feature.danggn.DanggnViewModel
import com.mashup.feature.danggn.ShakeDanggnScreen
import com.mashup.feature.danggn.ranking.DanggnRankingViewModel
import com.mashup.feature.danggn.reward.DanggnRewardPopup
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class ShakeDanggnActivity : BaseActivity<ActivityShakeDanggnBinding>() {
    override val layoutId: Int = R.layout.activity_shake_danggn

    private val viewModel: DanggnViewModel by viewModels()
    private val rankingViewModel: DanggnRankingViewModel by viewModels()

    override fun initViews() {
        super.initViews()
        sendActivityEnterType(LOG_DANGGN)

        viewBinding.shakeDanggnScreen.setContent {
            MashUpTheme {
                ShakeDanggnScreen(
                    modifier = Modifier.fillMaxSize(),
                    viewModel = viewModel,
                    rankingViewModel = rankingViewModel,
                    onClickBackButton = this::onBackPressed,
                    onClickDanggnInfoButton = this::openDanggnInfoActivity,
                    onClickHelpButton = this::openDanggnUpdateActivity,
                    onClickAnotherRounds = this::showDanggnRoundSelectDialog,
                    onClickReward = this::showDanggnRewardPopup
                )
            }
        }
    }

    override fun initObserves() {
        super.initObserves()
        flowLifecycleScope {
            viewModel.uiState.collectLatest { state ->
                when (state) {
                    is DanggnUiState.Error -> {
                        handleCommonError(state.code)
                    }

                    else -> {}
                }
            }
        }
    }

    private fun openDanggnUpdateActivity() {
        val intent = DanggnUpdateActivity.newIntent(context = this, hasMoveToDanggnButton = false)
        startActivity(intent)
    }

    private fun openDanggnInfoActivity() {
        sendActivityEnterType(LOG_DANGGN_HELP)
        val intent = DanggnInfoActivity.newIntent(this)
        startActivity(intent)
    }

    private fun showDanggnRoundSelectDialog() {
        DanggnRoundSelectorDialog().show(
            supportFragmentManager,
            DanggnRoundSelectorDialog::class.simpleName
        )
    }

    private fun showDanggnRewardPopup() {
        DanggnRewardPopup().show(supportFragmentManager, DanggnRewardPopup::class.simpleName)
    }

    companion object {
        fun newIntent(
            context: Context,
            type: ActivityEnterType = ActivityEnterType.NORMAL
        ) = Intent(context, ShakeDanggnActivity::class.java).apply {
            putExtra(EXTRA_ANIMATION, NavigationAnimationType.SLIDE)
            putExtra(EXTRA_ACTIVITY_ENTER_TYPE, type.name)
        }
    }
}
