package com.mashup.ui.setting

import android.content.Context
import android.content.Intent
import androidx.activity.viewModels
import com.mashup.R
import com.mashup.base.BaseActivity
import com.mashup.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingActivity : BaseActivity<ActivityMainBinding>() {
    override val layoutId = R.layout.activity_setting

    private val viewModel: SettingViewModel by viewModels()

    companion object {
        fun start(context: Context?, action: Intent.() -> Unit = {}) {
            val intent = Intent(context, SettingActivity::class.java).apply(action)
            context?.startActivity(intent)
        }
    }

}
