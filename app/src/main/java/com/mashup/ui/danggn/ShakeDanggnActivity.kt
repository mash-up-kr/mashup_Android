package com.mashup.ui.danggn

import android.content.Context
import android.content.Intent
import com.mashup.R
import com.mashup.base.BaseActivity
import com.mashup.databinding.ActivityShakeDanggnBinding

class ShakeDanggnActivity : BaseActivity<ActivityShakeDanggnBinding>() {
    override val layoutId: Int = R.layout.activity_shake_danggn

    override fun initViews() {
        super.initViews()

        viewBinding.shakeDanggnScreen.setContent {

        }
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, ShakeDanggnActivity::class.java).apply {

        }
    }
}
