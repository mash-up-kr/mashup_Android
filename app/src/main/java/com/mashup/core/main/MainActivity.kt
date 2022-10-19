package com.mashup.core.main

import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.mashup.R
import com.mashup.base.BaseActivity
import com.mashup.core.common.extensions.onThrottleFirstClick
import com.mashup.core.common.extensions.setStatusBarColorRes
import com.mashup.core.common.extensions.setStatusBarDarkTextColor
import com.mashup.core.common.model.NavigationAnimationType
import com.mashup.constant.EXTRA_ANIMATION
import com.mashup.databinding.ActivityMainBinding
import com.mashup.core.main.model.MainTab
import com.mashup.core.qrscan.CongratsAttendanceScreen
import com.mashup.core.qrscan.QRScanActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {
    override val layoutId = R.layout.activity_main

    private val viewModel: MainViewModel by viewModels()

    private val navController by lazy {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.main_container) as NavHostFragment
        navHostFragment.navController
    }

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
    }

    private fun initComposeView() {
        viewBinding.composeView.setContent {
            CongratsAttendanceScreen(
                isVisible = viewModel.isShowCongratsAttendanceScreen.value
            )
        }
    }

    private fun initTabButtons() = with(viewBinding) {
        layoutMainTab.btnQrcode.onThrottleFirstClick(lifecycleScope) {
            qrcodeLauncher.launch(
                QRScanActivity.newIntent(this@MainActivity)
            )
        }
        layoutMainTab.sectionEvent.onThrottleFirstClick(lifecycleScope) {
            viewModel.setMainTab(MainTab.EVENT)
        }
        layoutMainTab.sectionMyPage.onThrottleFirstClick(lifecycleScope) {
            viewModel.setMainTab(MainTab.MY_PAGE)
        }
    }

    override fun initObserves() {
        super.initObserves()
        flowLifecycleScope {
            viewModel.mainTab.collectLatest { tab ->
                navigationTab(tab)
                setUIOfTab(tab)
                updateStatusBarColor(tab)
            }
        }
    }

    private fun navigationTab(toDestination: MainTab) {
        val currentNavigationId = navController.currentDestination?.id
        val newNavigationId = when (toDestination) {
            MainTab.EVENT -> {
                R.id.eventFragment
            }
            MainTab.MY_PAGE -> {
                R.id.myPageFragment
            }
        }
        if (currentNavigationId != newNavigationId) {
            navController.navigate(newNavigationId)
        }
    }

    private fun setUIOfTab(tab: MainTab) = with(viewBinding.layoutMainTab) {
        val selectedColor = ContextCompat.getColor(this@MainActivity, R.color.gray800)
        val selectedColorList = ContextCompat.getColorStateList(this@MainActivity, R.color.gray800)
        val unSelectedColor = ContextCompat.getColor(this@MainActivity, R.color.gray500)
        val unSelectedColorList =
            ContextCompat.getColorStateList(this@MainActivity, R.color.gray500)

        when (tab) {
            MainTab.EVENT -> {
                tvEvent.setTextColor(selectedColor)
                imgEvent.imageTintList = selectedColorList
                tvMyPage.setTextColor(unSelectedColor)
                imgMyPage.imageTintList = unSelectedColorList
            }
            MainTab.MY_PAGE -> {
                tvEvent.setTextColor(unSelectedColor)
                imgEvent.imageTintList = unSelectedColorList
                tvMyPage.setTextColor(selectedColor)
                imgMyPage.imageTintList = selectedColorList
            }
        }
    }

    private fun updateStatusBarColor(tab: MainTab) {
        when (tab) {
            MainTab.EVENT -> {
                setStatusBarColorRes(R.color.gray50)
                setStatusBarDarkTextColor(true)
            }
            MainTab.MY_PAGE -> {
                setStatusBarColorRes(R.color.gray950)
                setStatusBarDarkTextColor(false)
            }
        }
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, MainActivity::class.java).apply {
            putExtra(EXTRA_ANIMATION, NavigationAnimationType.PULL)
        }
    }
}
