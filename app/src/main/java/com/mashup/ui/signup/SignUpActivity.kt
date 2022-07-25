package com.mashup.ui.signup

import androidx.navigation.fragment.NavHostFragment
import com.mashup.R
import com.mashup.base.BaseActivity
import com.mashup.databinding.ActivitySignUpBinding
import com.mashup.widget.CommonDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpActivity : BaseActivity<ActivitySignUpBinding>() {

    private val navController by lazy {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navHostFragment.navController
    }

    override fun initViews() {
        viewBinding.toolbar.setOnBackButtonClickListener {
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        if (!navController.popBackStack()) {
            CommonDialog(this).apply {
                setTitle(text = "회원가입을 그만두시겠어요?")
                setMessage(text = "입력한 전체 내용이 삭제됩니다.")
                setNegativeButton()
                setPositiveButton {
                    finish()
                }
                show()
            }
        }
    }

    override val layoutId: Int
        get() = R.layout.activity_sign_up
}