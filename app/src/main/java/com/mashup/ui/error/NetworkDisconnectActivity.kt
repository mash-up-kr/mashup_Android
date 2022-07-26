package com.mashup.ui.error

import android.content.Context
import android.content.Intent
import com.mashup.R
import com.mashup.base.BaseActivity
import com.mashup.databinding.ActivityNetworkDisconnectBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NetworkDisconnectActivity : BaseActivity<ActivityNetworkDisconnectBinding>() {

    override fun initViews() {
        super.initViews()
        initButtons()
    }

    private fun initButtons() {
        viewBinding.btnClose.setOnClickListener {
            finish()
        }
        viewBinding.btnRetry.setButtonText(
            getString(R.string.text_button_retry)
        )
        viewBinding.btnRetry.setOnClickListener {
            finish()
        }
    }

    override val layoutId: Int
        get() = R.layout.activity_network_disconnect

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, NetworkDisconnectActivity::class.java)
        }
    }
}