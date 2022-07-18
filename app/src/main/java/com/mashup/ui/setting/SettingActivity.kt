package com.mashup.ui.setting

import android.content.Context
import android.content.Intent
import android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import com.mashup.R
import com.mashup.base.BaseActivity
import com.mashup.databinding.ActivitySettingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingActivity : BaseActivity<ActivitySettingBinding>() {

    private val viewModel: SettingViewModel by viewModels()

    override fun initViews() {
        initButton()
        window.setSoftInputMode(SOFT_INPUT_ADJUST_NOTHING);
    }

    private fun initButton() {
        viewBinding.toolbar.setOnBackButtonClickListener {
            onBackPressed()
        }
        viewBinding.btnWithdrawal.setOnClickListener {
            changeOutContainerFragment(WithdrawalFragment.newInstance())
        }
    }

    private fun changeOutContainerFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, fragment)
        }.commit()
    }

    companion object {
        fun start(context: Context?, action: Intent.() -> Unit = {}) {
            val intent = Intent(context, SettingActivity::class.java).apply(action)
            context?.startActivity(intent)
        }
    }

    override val layoutId = R.layout.activity_setting
}
