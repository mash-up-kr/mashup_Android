package com.mashup.core.login

import android.content.Context
import android.content.Intent
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.mashup.R
import com.mashup.base.BaseActivity
import com.mashup.common.extensions.onThrottleFirstClick
import com.mashup.common.model.Validation
import com.mashup.constant.EXTRA_LOGOUT
import com.mashup.constant.EXTRA_WITH_DRAWL
import com.mashup.databinding.ActivityLoginBinding
import com.mashup.network.errorcode.MEMBER_NOT_FOUND
import com.mashup.network.errorcode.MEMBER_NOT_MATCH_PASSWORD
import com.mashup.core.main.MainActivity
import com.mashup.core.signup.SignUpActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding>() {
    private val viewModel: LoginViewModel by viewModels()

    override fun initViews() {
        initLoginState()
        initTextField()
        initButtons()
    }

    private fun initLoginState() {
        val isRequestLogOut = intent.getBooleanExtra(EXTRA_LOGOUT, false)
        val isRequestWithDrawl = intent.getBooleanExtra(EXTRA_WITH_DRAWL, false)

        if (isRequestLogOut || isRequestWithDrawl) {
            viewModel.clearUserData()
        }

        when {
            isRequestWithDrawl -> {
                showToast("회원탈퇴 완료되었어요")
            }
            isRequestLogOut -> {
                showToast("로그아웃 되었어요")
            }
        }
    }

    override fun initObserves() {
        viewModel.run {
            flowLifecycleScope {
                loginUiState.collect { state ->
                    when (state) {
                        is LoginState.Loading -> {
                            viewBinding.btnLogin.showLoading()
                        }
                        LoginState.Success -> {
                            viewBinding.btnLogin.hideLoading()
                            moveToMainScreen()
                        }
                        is LoginState.Error -> {
                            viewBinding.btnLogin.hideLoading()
                            handleCommonError(state.code)
                            handleSignUpErrorCode(state)
                        }
                        else -> {
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
                SignUpActivity.newIntent(this)
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
                null
            }
        }
        codeMessage?.run { showToast(codeMessage) }
    }

    override val layoutId: Int = R.layout.activity_login

    companion object {

        fun newIntent(
            context: Context,
            isLogOut: Boolean = false,
            isWithDrawl: Boolean = false
        ): Intent {
            return Intent(context, LoginActivity::class.java).apply {
                putExtra(EXTRA_LOGOUT, isLogOut)
                putExtra(EXTRA_WITH_DRAWL, isWithDrawl)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
        }
    }
}
