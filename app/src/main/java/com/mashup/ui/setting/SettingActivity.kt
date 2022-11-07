package com.mashup.ui.setting

import android.content.Context
import android.content.Intent
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.mashup.R
import com.mashup.base.BaseActivity
import com.mashup.constant.EXTRA_ANIMATION
import com.mashup.core.common.extensions.onThrottleFirstClick
import com.mashup.core.common.model.NavigationAnimationType
import com.mashup.core.common.widget.CommonDialog
import com.mashup.databinding.ActivitySettingBinding
import com.mashup.ui.login.LoginActivity
import com.mashup.ui.withdrawl.WithdrawalActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingActivity : BaseActivity<ActivitySettingBinding>() {

    private val viewModel: SettingViewModel by viewModels()

    override fun initViews() {
        initDataBinding()
        initButton()
    }

    private fun initDataBinding() {
        viewBinding.viewModel = viewModel
    }

    private fun initButton() {
        viewBinding.toolbar.setOnBackButtonClickListener {
            onBackPressed()
        }
        viewBinding.btnLogout.setOnClickListener {
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
        viewBinding.btnWithdrawal.onThrottleFirstClick(lifecycleScope) {
            startActivity(
                WithdrawalActivity.newInstance(this)
            )
        }
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, SettingActivity::class.java).apply {
            putExtra(EXTRA_ANIMATION, NavigationAnimationType.PULL)
        }
    }

    override val layoutId = R.layout.activity_setting
}
