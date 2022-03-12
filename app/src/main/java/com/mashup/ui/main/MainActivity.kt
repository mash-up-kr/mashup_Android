package com.mashup.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mashup.R
import com.mashup.base.BaseActivity
import com.mashup.databinding.ActivityMainBinding
import com.mashup.ui.mypage.MyPageFragment
import com.mashup.ui.qrscan.QRScanFragment
import com.mashup.ui.schedule.ScheduleFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {
    override val layoutId = R.layout.activity_main

    companion object {
        private val mainPagerList = MainType.values()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    private fun initView() = with(viewBinding) {
        vpMain.adapter = MainPagerAdapter(this@MainActivity)
    }

    private inner class MainPagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = mainPagerList.size

        override fun createFragment(position: Int): Fragment {
            return when (mainPagerList[position]) {
                MainType.QRCODE -> QRScanFragment.newInstance()
                MainType.SCHEDULE -> ScheduleFragment.newInstance()
                MainType.MY_PAGE -> MyPageFragment.newInstance()
            }
        }
    }
}