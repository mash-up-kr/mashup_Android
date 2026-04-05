package com.mashup.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.app.TaskStackBuilder
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mashup.R
import com.mashup.base.BaseComposeActivity
import com.mashup.constant.EXTRA_LINK
import com.mashup.constant.EXTRA_LOGOUT
import com.mashup.constant.EXTRA_MAIN_TAB
import com.mashup.constant.EXTRA_WITH_DRAWL
import com.mashup.constant.log.LOG_LOGIN
import com.mashup.constant.log.LOG_SIGN_UP
import com.mashup.core.common.constant.MEMBER_NOT_FOUND
import com.mashup.core.common.constant.MEMBER_NOT_MATCH_PASSWORD
import com.mashup.core.common.model.ActivityEnterType
import com.mashup.service.PushLinkType
import com.mashup.ui.danggn.ShakeDanggnActivity
import com.mashup.ui.main.MainActivity
import com.mashup.ui.main.model.MainTab
import com.mashup.ui.password.PasswordActivity
import com.mashup.ui.qrscan.QRScanActivity
import com.mashup.ui.signup.SignUpActivity
import com.mashup.ui.webview.birthday.BirthdayActivity
import com.mashup.ui.webview.mashong.MashongActivity
import com.mashup.util.AnalyticsManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : BaseComposeActivity() {
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initLoginState()
    }

    @Composable
    override fun MainContainer() {
        val inputFieldState by viewModel.inputFieldState.collectAsStateWithLifecycle()
        val loginUiState by viewModel.loginUiState.collectAsStateWithLifecycle()
        val loginValidationState by viewModel.loginValidation.collectAsStateWithLifecycle()

        LaunchedEffect(loginUiState) {
            when (val state = loginUiState) {
                is LoginState.Success -> {
                    moveNextScreen(state.loginType)
                }

                is LoginState.Error -> {
                    handleCommonError(state.code)
                    handleSignUpErrorCode(state)
                }

                else -> Unit
            }
        }

        LoginScreen(
            id = inputFieldState.first,
            password = inputFieldState.second,
            loginState = loginUiState,
            validation = loginValidationState,
            setId = { text ->
                viewModel.setId(text)
            },
            setPassword = { text ->
                viewModel.setPwd(text)
            },
            onClickLogin = { id, pwd ->
                AnalyticsManager.addEvent(eventName = LOG_LOGIN)
                viewModel.requestLogin(
                    id = id,
                    pwd = pwd
                )
            },
            onClickSignUp = {
                AnalyticsManager.addEvent(eventName = LOG_SIGN_UP)
                startActivity(
                    SignUpActivity.newIntent(this@LoginActivity)
                )
            },
            onClickChangePassword = {
                startActivity(
                    PasswordActivity.newIntent(this@LoginActivity)
                )
            },
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.onBackground)
                .statusBarsPadding()
                .navigationBarsPadding()
        )
    }

    private fun initLoginState() {
        val isRequestLogOut = intent.getBooleanExtra(EXTRA_LOGOUT, false)
        val isRequestWithDrawl = intent.getBooleanExtra(EXTRA_WITH_DRAWL, false)

        when {
            isRequestWithDrawl -> {
                showToast(getString(R.string.withdrawal_complete))
            }

            isRequestLogOut -> {
                showToast(getString(R.string.logout_complete))
            }
        }
    }

    private fun handleSignUpErrorCode(error: LoginState.Error) {
        val codeMessage = when (error.code) {
            MEMBER_NOT_FOUND -> {
                getString(R.string.member_not_found)
            }

            MEMBER_NOT_MATCH_PASSWORD -> {
                getString(R.string.member_not_match_password)
            }

            else -> {
                null
            }
        }
        codeMessage?.run { showToast(codeMessage) }
    }

    private fun moveNextScreen(loginType: LoginType) {
        val deepLink = intent.getStringExtra(EXTRA_LINK).orEmpty()
        val baseIntent = MainActivity.newIntent(
            context = this,
            loginType = loginType,
            mainTab = (intent.getSerializableExtra(EXTRA_MAIN_TAB) as? MainTab) ?: MainTab.EVENT
        )
        val taskStackBuilder = when (val pushType = PushLinkType.getPushLinkType(deepLink)) {
            PushLinkType.DANGGN, PushLinkType.DANGGN_REWARD -> {
                buildTaskStack(
                    baseIntent,
                    ShakeDanggnActivity.newIntent(
                        context = this,
                        showDanggnRewardNotice = pushType == PushLinkType.DANGGN_REWARD,
                        type = ActivityEnterType.ALARM
                    )
                )
            }

            PushLinkType.BIRTHDAY -> {
                buildTaskStack(baseIntent, BirthdayActivity.newIntent(this))
            }

            PushLinkType.MASHONG -> {
                buildTaskStack(baseIntent, MashongActivity.newIntent(this))
            }

            PushLinkType.QR -> {
                buildTaskStack(baseIntent, QRScanActivity.newIntent(this))
            }

            else -> {
                buildTaskStack(baseIntent)
            }
        }
        taskStackBuilder.startActivities()
        finish()
    }

    private fun buildTaskStack(baseIntent: Intent, vararg newIntent: Intent) =
        TaskStackBuilder.create(this)
            .apply {
                addNextIntentWithParentStack(baseIntent)
                newIntent.forEach { intent ->
                    addNextIntent(intent)
                }
            }

    companion object {

        fun newIntent(
            context: Context,
            isLogout: Boolean = false,
            isWithDrawl: Boolean = false,
            mainTab: MainTab = MainTab.EVENT,
            deepLink: String = PushLinkType.UNKNOWN.name
        ): Intent {
            return Intent(context, LoginActivity::class.java).apply {
                putExtra(EXTRA_LOGOUT, isLogout)
                putExtra(EXTRA_WITH_DRAWL, isWithDrawl)
                putExtra(EXTRA_MAIN_TAB, mainTab)
                putExtra(EXTRA_LINK, deepLink)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
        }
    }
}
