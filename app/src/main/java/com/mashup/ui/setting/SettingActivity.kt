package com.mashup.ui.setting

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.mashup.R
import com.mashup.base.BaseActivity
import com.mashup.constant.EXTRA_ANIMATION
import com.mashup.core.common.model.NavigationAnimationType
import com.mashup.core.common.widget.CommonDialog
import com.mashup.core.ui.theme.MashUpTheme
import com.mashup.databinding.ActivitySettingBinding
import com.mashup.ui.login.LoginActivity
import com.mashup.ui.withdrawl.WithdrawalActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class SettingActivity : BaseActivity<ActivitySettingBinding>() {

    private val viewModel: SettingViewModel by viewModels()

    override fun initViews() {
        super.initViews()
        initButton()

        viewBinding.settingScreen.setContent {
            MashUpTheme {
                SettingScreen(
                    modifier = Modifier.fillMaxSize(),
                    onLogout = this::showLogoutDialog,
                    onDeleteUser = this::moveToDeleteAccount,
                    onToggleFcm = this::onToggleFcm,
                    onClickSNS = this::onClickSNS
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

    private fun initButton() {
        viewBinding.toolbar.setOnBackButtonClickListener {
            onBackPressed()
        }
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
        startActivity(
            WithdrawalActivity.newInstance(this)
        )
    }

    private fun onClickSNS(link: String) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(link)))
    }

    private fun onToggleFcm() {
        // 서버 요청날리기
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, SettingActivity::class.java).apply {
            putExtra(EXTRA_ANIMATION, NavigationAnimationType.PULL)
        }
    }

    override val layoutId = R.layout.activity_setting
}
