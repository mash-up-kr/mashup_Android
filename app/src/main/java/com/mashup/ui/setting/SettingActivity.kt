package com.mashup.ui.setting

import android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.mashup.R
import com.mashup.base.BaseActivity
import com.mashup.databinding.ActivitySettingBinding
import com.mashup.extensions.onThrottleFirstClick
import com.mashup.ui.login.LoginActivity
import com.mashup.ui.withdrawl.WithdrawalActivity
import com.mashup.widget.CommonDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingActivity : BaseActivity<ActivitySettingBinding>() {

    private val viewModel: SettingViewModel by viewModels()

    override fun initViews() {
        initDataBinding()
        initButton()
        window.setSoftInputMode(SOFT_INPUT_ADJUST_NOTHING)
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
                        LoginActivity.newIntent(this@SettingActivity)
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

    override val layoutId = R.layout.activity_setting
}
