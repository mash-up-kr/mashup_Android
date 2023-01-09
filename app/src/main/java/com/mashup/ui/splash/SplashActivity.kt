package com.mashup.ui.splash

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.mashup.R
import com.mashup.base.BaseActivity
import com.mashup.core.common.extensions.setStatusBarColorRes
import com.mashup.core.common.extensions.setStatusBarDarkTextColor
import com.mashup.data.repository.UserRepository
import com.mashup.databinding.ActivitySplashBinding
import com.mashup.ui.login.LoginActivity
import com.mashup.util.AnalyticsManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import javax.inject.Inject

/**
 * Splash API 사용 시 Icon이 잘리는 현상이 있어 차선책으로 사용
 */
@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : BaseActivity<ActivitySplashBinding>() {

    @Inject
    lateinit var userRepository: UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launchWhenResumed {
            delay(2000)
            startActivity(LoginActivity.newIntent(this@SplashActivity))
        }
        setUi()
        initAnalyticsManager()
    }

    private fun setUi() {
        setStatusBarColorRes(com.mashup.core.common.R.color.brand500)
        setStatusBarDarkTextColor(false)
    }

    private fun initAnalyticsManager() {
        AnalyticsManager.setUserId(
            userId = userRepository.getUserMemberId(),
        )
        AnalyticsManager.setUserToken(
            userToken = userRepository.getUserToken()
        )
    }

    override val layoutId: Int = R.layout.activity_splash
}
