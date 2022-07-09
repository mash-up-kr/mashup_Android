package com.mashup.ui.setting

import androidx.activity.viewModels
import com.mashup.R
import com.mashup.base.BaseActivity
import com.mashup.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingActivity : BaseActivity<ActivityMainBinding>() {
    override val layoutId = R.layout.activity_setting

    private val viewModel: SettingViewModel by viewModels()
}
