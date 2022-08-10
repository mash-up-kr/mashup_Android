package com.mashup.ui.signup

import android.content.Context
import android.content.Intent
import androidx.activity.viewModels
import androidx.navigation.fragment.NavHostFragment
import com.mashup.R
import com.mashup.base.BaseActivity
import com.mashup.common.NavigationAnimationType
import com.mashup.constant.EXTRA_ANIMATION
import com.mashup.databinding.ActivitySignUpBinding
import com.mashup.widget.CommonDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class SignUpActivity : BaseActivity<ActivitySignUpBinding>() {

    private val viewModel: SignUpViewModel by viewModels()

    private var navigationAnimationType = NavigationAnimationType.SLIDE

    private val navController by lazy {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navHostFragment.navController
    }

    override fun initViews() {
        initToolbar()
    }

    private fun initToolbar() {
        viewBinding.toolbar.setOnBackButtonClickListener {
            navigationAnimationType = NavigationAnimationType.SLIDE
            onBackPressed()
        }
        viewBinding.toolbar.setOnCloseButtonClickListener {
            navigationAnimationType = NavigationAnimationType.PULL
            if (!viewModel.isDataEmpty()) {
                showExitPopup {
                    finish()
                }
            } else {
                finish()
            }
        }
    }

    override fun initObserves() {
        super.initObserves()
        flowLifecycleScope {
            viewModel.showToolbarDivider.collectLatest { isVisible ->
                if (isVisible) {
                    viewBinding.toolbar.showDivider()
                } else {
                    viewBinding.toolbar.hideDivider()
                }
            }
        }
    }

    override fun onBackPressed() {
        showExitPopup {
            if (!navController.popBackStack()) {
                finish()
            }
        }
    }

    private fun showExitPopup(action: () -> Unit) {
        CommonDialog(this).apply {
            setTitle(text = "회원가입을 그만두시겠어요?")
            setMessage(text = "입력한 전체 내용이 삭제됩니다.")
            setNegativeButton()
            setPositiveButton {
                action()
            }
            show()
        }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(
            0,
            navigationAnimationType.exitOut
        )
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, SignUpActivity::class.java).apply {
            putExtra(EXTRA_ANIMATION, NavigationAnimationType.SLIDE)
        }
    }

    override val layoutId: Int
        get() = R.layout.activity_sign_up
}