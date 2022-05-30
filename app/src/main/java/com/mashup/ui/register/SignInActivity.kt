package com.mashup.ui.register

import androidx.navigation.fragment.NavHostFragment
import com.mashup.R
import com.mashup.base.BaseActivity
import com.mashup.databinding.ActivitySignInBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInActivity : BaseActivity<ActivitySignInBinding>() {

    private val navController by lazy {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navHostFragment.navController
    }

    override fun initViews() {
        viewBinding.toolbar.setOnBackButtonClickListener {
            if (navController.backQueue.size > 1) {
                navController.popBackStack()
            } else {
                finish()
            }
        }
    }

    override val layoutId: Int
        get() = R.layout.activity_sign_in
}