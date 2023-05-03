package com.mashup.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.app.TaskStackBuilder
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.mashup.R
import com.mashup.base.BaseActivity
import com.mashup.constant.EXTRA_LINK
import com.mashup.core.common.extensions.setStatusBarColorRes
import com.mashup.core.common.extensions.setStatusBarDarkTextColor
import com.mashup.core.common.widget.CommonDialog
import com.mashup.databinding.ActivitySplashBinding
import com.mashup.datastore.data.repository.UserPreferenceRepository
import com.mashup.service.PushLinkType
import com.mashup.ui.danggn.ShakeDanggnActivity
import com.mashup.ui.login.LoginActivity
import com.mashup.ui.qrscan.QRScanActivity
import com.mashup.util.AnalyticsManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Splash API 사용 시 Icon이 잘리는 현상이 있어 차선책으로 사용
 */
@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : BaseActivity<ActivitySplashBinding>() {

    @Inject
    lateinit var userPreferenceRepository: UserPreferenceRepository

    private val splashViewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setUi()
        observeViewModel()
        initAnalyticsManager()
    }

    private fun setUi() {
        setStatusBarColorRes(com.mashup.core.common.R.color.brand500)
        setStatusBarDarkTextColor(false)
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    splashViewModel.onLowerAppVersion.collectLatest {
                        CommonDialog(this@SplashActivity).apply {
                            setTitle("업데이트")
                            setMessage("정상적인 이용을 위해\n업데이트가 필요합니다.")
                            setNegativeButton(text = "확인") {
                                moveNextScreen()
                            }
                            setPositiveButton(text = "업데이트 하기") {
                                moveGooglePayForUpdate()
                            }
                            show()
                        }
                    }
                }
                launch {
                    splashViewModel.onFinishInit.collectLatest {
                        moveNextScreen()
                    }
                }
            }
        }
        splashViewModel.checkAppVersion(this)
    }

    private fun moveNextScreen() {
        val deepLink = intent.getStringExtra(EXTRA_LINK) ?: ""
        val baseIntent = LoginActivity.newIntent(this@SplashActivity)
        val taskStackBuilder = when (PushLinkType.getPushLinkType(deepLink)) {
            PushLinkType.DANGGN -> {
                TaskStackBuilder.create(this)
                    .addNextIntentWithParentStack(baseIntent)
                    .addNextIntent(ShakeDanggnActivity.newIntent(this))
            }
            PushLinkType.QR -> {
                TaskStackBuilder.create(this)
                    .addNextIntentWithParentStack(baseIntent)
                    .addNextIntent(QRScanActivity.newIntent(this))
            }
            else -> {
                TaskStackBuilder.create(this)
                    .addNextIntentWithParentStack(baseIntent)
            }
        }
        taskStackBuilder.startActivities()
        finish()
    }

    private fun moveGooglePayForUpdate() {
        try {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(
                    "https://play.google.com/store/apps/details?id=com.mashup"
                )
                setPackage("com.android.vending")
            }
            startActivity(intent)
            finish()
        } catch (ignore: Exception) {
        }
    }

    private fun initAnalyticsManager() {
        AnalyticsManager.init(this)
        lifecycleScope.launch {
            AnalyticsManager.setUserToken(
                userToken = userPreferenceRepository.getUserPreference().first().token
            )
        }
    }

    override val layoutId: Int = R.layout.activity_splash
}
