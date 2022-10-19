package com.mashup.core.splash

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.mashup.R
import com.mashup.base.BaseActivity
import com.mashup.common.extensions.setStatusBarColorRes
import com.mashup.common.extensions.setStatusBarDarkTextColor
import com.mashup.databinding.ActivitySplashBinding
import com.mashup.core.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

/**
 * Splash API 사용 시 Icon이 잘리는 현상이 있어 차선책으로 사용
 */
@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : BaseActivity<ActivitySplashBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launchWhenResumed {
            delay(2000)
            startActivity(LoginActivity.newIntent(this@SplashActivity))
        }
        setUi()
    }

    private fun setUi() {
        setStatusBarColorRes(R.color.brand500)
        setStatusBarDarkTextColor(false)
    }

    override val layoutId: Int = R.layout.activity_splash
}
