package com.mashup.ui.login

import android.os.Bundle
import com.mashup.R
import com.mashup.base.BaseActivity
import com.mashup.databinding.ActivityLoginBinding

class LoginActivity : BaseActivity<ActivityLoginBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override val layoutId: Int
        get() = R.layout.activity_login
}