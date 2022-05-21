package com.mashup.ui.main

import androidx.lifecycle.lifecycleScope
import com.mashup.R
import com.mashup.base.BaseActivity
import com.mashup.databinding.ActivityMainBinding
import com.mashup.extensions.onDebouncedClick
import com.mashup.ui.qrscan.QRScanActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {
    override val layoutId = R.layout.activity_main

    override fun initViews() {
        super.initViews()
        initTabButtons()
    }

    private fun initTabButtons() = with(viewBinding) {
        btnQRScan.onDebouncedClick(lifecycleScope) {
            startActivity(QRScanActivity.newIntent(this@MainActivity))
        }
    }
}