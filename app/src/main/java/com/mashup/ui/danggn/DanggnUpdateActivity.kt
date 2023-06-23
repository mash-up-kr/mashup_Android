package com.mashup.ui.danggn

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.mashup.R
import com.mashup.base.BaseActivity
import com.mashup.constant.EXTRA_ANIMATION
import com.mashup.core.common.model.ActivityEnterType
import com.mashup.core.common.model.NavigationAnimationType
import com.mashup.core.ui.theme.MashUpTheme
import com.mashup.databinding.ActivityDanggnInfoBinding
import com.mashup.feature.danggn.ui.DanggnUpdateScreen

class DanggnUpdateActivity : BaseActivity<ActivityDanggnInfoBinding>() {
    override val layoutId: Int = R.layout.activity_danggn_info

    override fun initViews() {
        super.initViews()

        viewBinding.shakeDanggnScreen.setContent {
            MashUpTheme {
                DanggnUpdateScreen(
                    modifier = Modifier.fillMaxSize(),
                    onClickCloseButton = {
                        finish()
                    },
                    onClickMoveDanggn = {
                        startActivity(
                            ShakeDanggnActivity.newIntent(this, ActivityEnterType.UPDATE)
                        )
                        finish()
                    }
                )
            }
        }
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, DanggnUpdateActivity::class.java).apply {
            putExtra(EXTRA_ANIMATION, NavigationAnimationType.PULL)
        }
    }
}
