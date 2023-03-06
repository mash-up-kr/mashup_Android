package com.mashup.ui.setting

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.mashup.R
import com.mashup.URL
import com.mashup.base.BaseActivity
import com.mashup.constant.EXTRA_ANIMATION
import com.mashup.constant.log.LOG_DELETE_USER
import com.mashup.constant.log.LOG_LOGOUT
import com.mashup.constant.log.LOG_SNS_FACEBOOK
import com.mashup.constant.log.LOG_SNS_INSTAGRAM
import com.mashup.constant.log.LOG_SNS_MASHUP_HOME
import com.mashup.constant.log.LOG_SNS_MASHUP_RECRUIT
import com.mashup.constant.log.LOG_SNS_TISTORY
import com.mashup.constant.log.LOG_SNS_YOUTUBE
import com.mashup.core.common.model.NavigationAnimationType
import com.mashup.core.common.widget.CommonDialog
import com.mashup.core.model.data.local.UserPreference
import com.mashup.core.ui.theme.MashUpTheme
import com.mashup.databinding.ActivitySettingBinding
import com.mashup.feature.SettingScreen
import com.mashup.ui.login.LoginActivity
import com.mashup.ui.withdrawl.WithdrawalActivity
import com.mashup.util.AnalyticsManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class SettingActivity : BaseActivity<ActivitySettingBinding>() {

    private val viewModel: SettingViewModel by viewModels()

    override fun initViews() {
        super.initViews()

        viewBinding.settingScreen.setContent {
            MashUpTheme {
                val userPreference by viewModel.userPreference.collectAsState(
                    initial = UserPreference.getDefaultInstance()
                )

                SettingScreen(
                    modifier = Modifier.fillMaxSize(),
                    onLogout = this::onClickLogoutButton,
                    onDeleteUser = this::moveToDeleteAccount,
                    onToggleFcm = this::onToggleFcm,
                    onClickSNS = this::onClickSNS,
                    userPreference = userPreference,
                    onClickBackButton = {
                        onBackPressed()
                    }
                )
            }
        }
    }

    override fun initObserves() {
        super.initObserves()
        flowLifecycleScope {
            viewModel.onSuccessLogout.collectLatest {
                moveToLoginActivityOnLogout()
            }
        }
    }

    private fun onClickLogoutButton() {
        AnalyticsManager.addEvent(LOG_LOGOUT)
        showLogoutDialog()
    }

    private fun showLogoutDialog() {
        CommonDialog(this).apply {
            setTitle(text = "로그아웃 하시겠습니까?")
            setNegativeButton()
            setPositiveButton {
                viewModel.requestLogout()
            }
            show()
        }
    }

    private fun moveToLoginActivityOnLogout() {
        finish()
        startActivity(
            LoginActivity.newIntent(
                context = this@SettingActivity,
                isLogout = true
            )
        )
    }

    private fun moveToDeleteAccount() {
        AnalyticsManager.addEvent(LOG_DELETE_USER)
        startActivity(
            WithdrawalActivity.newInstance(this)
        )
    }

    private fun onClickSNS(link: String) {
        val eventLog = when (link) {
            URL.FACEBOOK -> LOG_SNS_FACEBOOK
            URL.INSTAGRAM -> LOG_SNS_INSTAGRAM
            URL.TISTORY -> LOG_SNS_TISTORY
            URL.YOUTUBE -> LOG_SNS_YOUTUBE
            URL.MASHUP_UP_HOME -> LOG_SNS_MASHUP_HOME
            URL.MASHUP_UP_RECRUIT -> LOG_SNS_MASHUP_RECRUIT
            else -> null
        }
        eventLog?.run { AnalyticsManager.addEvent(this) }
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(link)))
    }

    private fun onToggleFcm(isChecked: Boolean) {
        viewModel.patchPushNotification(isChecked)
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, SettingActivity::class.java).apply {
            putExtra(EXTRA_ANIMATION, NavigationAnimationType.PULL)
        }
    }

    override val layoutId = R.layout.activity_setting
}
