package com.mashup.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.mashup.R
import com.mashup.base.BaseActivity
import com.mashup.databinding.ActivityLoginBinding
import com.mashup.ui.main.MainActivity
import com.mashup.ui.signup.SignUpActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding>() {
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
    }

    override fun initViews() {
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
                        LoginUiState.LoginSuccess -> {
                            moveToMainScreen()
                        }
                    }
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

    private fun initButtons() {
        viewBinding.btnLogin.setOnButtonClickListener {
            viewModel.login(
                id = viewBinding.textFieldId.inputtedText,
                pwd = viewBinding.textFieldPwd.inputtedText
            )
        }

        viewBinding.tvSignIn.setOnClickListener {
            startActivity(
                Intent(this, SignUpActivity::class.java)
            )
        }
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
        fun newIntent(context: Context): Intent {
            return Intent(context, LoginActivity::class.java)
        }
    }
}