package com.mashup.ui.danggn

import android.content.Context
import android.content.Intent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.mashup.R
import com.mashup.base.BaseActivity
import com.mashup.core.ui.theme.MashUpTheme
import com.mashup.databinding.ActivityShakeDanggnBinding
import com.mashup.feature.danggn.DanggnViewModel
import com.mashup.feature.danggn.ShakeDanggnScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShakeDanggnActivity : BaseActivity<ActivityShakeDanggnBinding>() {
    override val layoutId: Int = R.layout.activity_shake_danggn

    private val viewModel: DanggnViewModel by viewModels()

    override fun initViews() {
        super.initViews()

        viewBinding.shakeDanggnScreen.setContent {
            MashUpTheme {
                ShakeDanggnScreen(
                    modifier = Modifier.fillMaxSize(),
                    viewModel = viewModel,
                    onClickBackButton = { onBackPressed() },
                    onClickDanggnInfoButton = { openDanggnInfoActivity() }
                )
            }
        }
    }

    private fun openDanggnInfoActivity() {
        val intent = DanggnInfoActivity.newIntent(this)
        startActivity(intent)
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, ShakeDanggnActivity::class.java).apply {

        }
    }
}
