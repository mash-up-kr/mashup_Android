package com.mashup.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.mashup.R
import com.mashup.base.BaseActivity
import com.mashup.common.Validation
import com.mashup.databinding.ActivityLoginBinding
import com.mashup.extensions.onThrottleFirstClick
import com.mashup.network.errorcode.MEMBER_NOT_FOUND
import com.mashup.network.errorcode.MEMBER_NOT_MATCH_PASSWORD
import com.mashup.ui.main.MainActivity
import com.mashup.ui.signup.SignUpActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding>() {
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        if (intent.getBooleanExtra(EXTRA_CLEAR_USER, false)) {
            viewModel.clearUserData()
            viewModel.ready()
        } else {
            viewModel.ready()
        }
    }

    override fun initViews() {
        initTextField()
        initSplash()
        initButtons()
        initSplashPreDraw()
    }

    private fun initSplash() {
        val content: View = findViewById(android.R.id.content)
        content.viewTreeObserver.addOnPreDrawListener(
            object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    return if (viewModel.isReady) {
                        content.viewTreeObserver.removeOnPreDrawListener(this)
                        true
                    } else {
                        false
                    }
                }
            }
        )
    }

    override fun initObserves() {
        viewModel.run {
            flowLifecycleScope {
                loginUiState.collect { state ->
                    when (state) {
                        LoginState.Success -> {
                            moveToMainScreen()
                        }
                        is LoginState.Error -> {
                            handleCommonError(state.code)
                            handleSignUpErrorCode(state)
                        }
                    }
                }
            }

            flowLifecycleScope {
                loginValidation.collectLatest {
                    viewBinding.btnLogin.setButtonEnabled(it == Validation.SUCCESS)
                }
            }
        }
    }

    private fun moveToMainScreen() {
        startActivity(
            Intent(this, MainActivity::class.java)
        )
        finish()
    }

    private fun initTextField() {
        viewBinding.textFieldId.run {
            addOnTextChangedListener { text ->
                viewModel.setId(text)
            }
        }

        viewBinding.textFieldPwd.run {
            addOnTextChangedListener { text ->
                viewModel.setPwd(text)
            }
        }
    }

    private fun initButtons() {
        viewBinding.btnLogin.setOnButtonThrottleFirstClickListener(this) {
            viewModel.requestLogin(
                id = viewBinding.textFieldId.inputtedText,
                pwd = viewBinding.textFieldPwd.inputtedText
            )
        }

        viewBinding.tvSignIn.onThrottleFirstClick(lifecycleScope) {
            startActivity(
                Intent(this, SignUpActivity::class.java)
            )
        }
    }

    private fun handleSignUpErrorCode(error: LoginState.Error) {
        val codeMessage = when (error.code) {
            MEMBER_NOT_FOUND -> {
                "회원을 찾을 수 없습니다."
            }
            MEMBER_NOT_MATCH_PASSWORD -> {
                "비밀번호가 일치하지 않습니다."
            }
            else -> {
                "잠시 후 다시 시도해주세요."
            }
        }
        showToast(codeMessage)
    }

    private fun initSplashPreDraw() {
        val content: View = findViewById(android.R.id.content)
        content.viewTreeObserver.addOnPreDrawListener(
            object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    content.viewTreeObserver.removeOnPreDrawListener(this)
                    return true
                }
            }
        )
    }

    override val layoutId: Int
        get() = R.layout.activity_login

    companion object {
        private const val EXTRA_CLEAR_USER = "EXTRA_CLEAR_USER"

        fun newIntent(context: Context): Intent {
            return Intent(context, LoginActivity::class.java).apply {
                putExtra(EXTRA_CLEAR_USER, true)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
        }
    }
}