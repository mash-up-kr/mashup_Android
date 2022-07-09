package com.mashup.ui.main

import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.mashup.R
import com.mashup.base.BaseActivity
import com.mashup.databinding.ActivityMainBinding
import com.mashup.ui.mypage.MyPageFragment
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.mashup.extensions.onDebouncedClick
import com.mashup.ui.qrscan.CongratsAttendanceScreen
import com.mashup.ui.qrscan.QRScanActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {
    override val layoutId = R.layout.activity_main
    val myPage = MyPageFragment.newInstance()

    private val viewModel: MainViewModel by viewModels()

    private val qrcodeLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            viewModel.successAttendance()
        }
    }

    override fun initViews() {
        super.initViews()
        initComposeView()
        initTabButtons()
        addFragment(myPage)
    }

    private fun initComposeView() {
        viewBinding.composeView.setContent {
            CongratsAttendanceScreen(
                isVisible = viewModel.isShowCongratsAttendanceScreen.value
            )
        }
    }

    private fun initTabButtons() = with(viewBinding) {
        btnQRScan.onDebouncedClick(lifecycleScope) {
            qrcodeLauncher.launch(
                QRScanActivity.newIntent(this@MainActivity)
            )
        }
    }

    private fun addFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            add(R.id.main_container, fragment)
        }.commit()
    }

    fun updateStatusBarColor(color: Int) {
        val window: Window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = color
    }
}