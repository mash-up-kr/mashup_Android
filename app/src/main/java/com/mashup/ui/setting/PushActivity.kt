package com.mashup.ui.setting

import android.content.Context
import android.content.Intent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.mashup.R
import com.mashup.base.BaseActivity
import com.mashup.constant.EXTRA_ANIMATION
import com.mashup.core.common.model.NavigationAnimationType
import com.mashup.core.ui.theme.MashUpTheme
import com.mashup.databinding.ActivitySettingBinding
import com.mashup.datastore.model.UserPreference
import com.mashup.feature.setting.ui.push.PushScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PushActivity : BaseActivity<ActivitySettingBinding>() {

    private val viewModel: SettingViewModel by viewModels()

    override fun initViews() {
        super.initViews()

        viewBinding.settingScreen.setContent {
            MashUpTheme {
                val userPreference by viewModel.userPreference.collectAsState(
                    initial = UserPreference.getDefaultInstance()
                )

                PushScreen(
                    modifier = Modifier.fillMaxSize(),
                    onToggleMashUpPush = this::onToggleMashUpPush,
                    onToggleDanggnPush = this::onToggleDanggnPush,
                    userPreference = userPreference,
                    onClickBackButton = {
                        onBackPressed()
                    }
                )
            }
        }
    }

    private fun onToggleMashUpPush(isChecked: Boolean) {
        viewModel.patchPushNotification(isChecked)
    }

    private fun onToggleDanggnPush(isChecked: Boolean) {
        viewModel.patchDanggnPushNotification(isChecked)
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, PushActivity::class.java).apply {
            putExtra(EXTRA_ANIMATION, NavigationAnimationType.SLIDE)
        }
    }

    override val layoutId = R.layout.activity_setting
}
