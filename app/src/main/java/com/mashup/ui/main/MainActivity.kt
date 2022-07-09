package com.mashup.ui.main

import android.view.Window
import android.view.WindowManager
import com.mashup.R
import com.mashup.base.BaseActivity
import com.mashup.databinding.ActivityMainBinding
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.mashup.extensions.onDebouncedClick
import com.mashup.ui.main.model.MainTab
import com.mashup.ui.qrscan.CongratsAttendanceScreen
import com.mashup.ui.qrscan.QRScanActivity
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
        layoutMainTab.btnQrcode.onDebouncedClick(lifecycleScope) {
            qrcodeLauncher.launch(
                QRScanActivity.newIntent(this@MainActivity)
            )
        }
        layoutMainTab.sectionEvent.onDebouncedClick(lifecycleScope) {
            viewModel.setMainTab(MainTab.EVENT)
        }
        layoutMainTab.sectionMyPage.onDebouncedClick(lifecycleScope) {
            viewModel.setMainTab(MainTab.MY_PAGE)
        }
    }

    override fun initObserves() {
        super.initObserves()
        flowLifecycleScope {
            viewModel.mainTab.collectLatest { tab ->
                navigationTab(tab)
                setUIOfTab(tab)
            }
        }
    }

    private fun navigationTab(toDestination: MainTab) {
        navController.navigate(
            when (toDestination) {
                MainTab.EVENT -> {
                    R.id.eventFragment
                }
                MainTab.MY_PAGE -> {
                    R.id.myPageFragment
                }
            }
        )
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

    fun updateStatusBarColor(color: Int) {
        val window: Window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = color
    }
}