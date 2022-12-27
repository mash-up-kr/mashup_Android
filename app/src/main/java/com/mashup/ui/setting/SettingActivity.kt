package com.mashup.ui.setting

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.fillMaxSize
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

@AndroidEntryPoint
class SettingActivity : BaseActivity<ActivitySettingBinding>() {

    override fun initViews() {
        initButton()

        viewBinding.settingScreen.setContent {
            MashUpTheme {
                SettingScreen(
                    modifier = Modifier.fillMaxSize(),
                    onLogout = this::showLogoutDialog,
                    onDeleteUser = this::moveToDeleteAccount,
                    onClickSNS = this::onClickSNS
                )
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
                startActivity(
                    LoginActivity.newIntent(
                        context = this@SettingActivity,
                        isLogOut = true
                    )
                )
                finish()
            }
            show()
        }
    }

    private fun moveToDeleteAccount() {
        startActivity(
            WithdrawalActivity.newInstance(this)
        )
    }

    private fun onClickSNS(link: String) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(link)))
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, SettingActivity::class.java).apply {
            putExtra(EXTRA_ANIMATION, NavigationAnimationType.PULL)
        }
    }

    override val layoutId = R.layout.activity_setting
}
