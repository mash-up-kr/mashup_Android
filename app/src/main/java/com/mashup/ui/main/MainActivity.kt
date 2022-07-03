package com.mashup.ui.main

import android.view.Window
import android.view.WindowManager
import androidx.fragment.app.Fragment
import com.mashup.R
import com.mashup.base.BaseActivity
import com.mashup.databinding.ActivityMainBinding
import com.mashup.ui.mypage.MyPageFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {
    override val layoutId = R.layout.activity_main
    val myPage = MyPageFragment.newInstance()

    override fun initViews() {
        super.initViews()
        initTabButtons()
        addFragment(myPage)
    }

    private fun initTabButtons() = with(viewBinding) {
//        btnQRScan.onDebouncedClick(lifecycleScope) {
//            startActivity(QRScanActivity.newIntent(this@MainActivity))
//        }
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