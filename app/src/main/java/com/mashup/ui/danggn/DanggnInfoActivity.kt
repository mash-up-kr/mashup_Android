package com.mashup.ui.danggn

import android.content.Context
import android.content.Intent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.mashup.R
import com.mashup.base.BaseActivity
import com.mashup.constant.EXTRA_ANIMATION
import com.mashup.core.common.model.NavigationAnimationType
import com.mashup.core.ui.theme.MashUpTheme
import com.mashup.databinding.ActivityDanggnInfoBinding
import com.mashup.feature.danggn.DanggnInfoScreen
import com.mashup.feature.mypage.profile.MyPageProfileEditViewModel
import com.mashup.feature.mypage.profile.edit.MyPageEditCardScreen
import com.mashup.feature.mypage.profile.edit.MyPageEditProfileScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DanggnInfoActivity : BaseActivity<ActivityDanggnInfoBinding>() {
    override val layoutId: Int = R.layout.activity_danggn_info
    private val editViewModel: MyPageProfileEditViewModel by viewModels()

    override fun initViews() {
        super.initViews()

        viewBinding.shakeDanggnScreen.setContent {
            MashUpTheme {
                MyPageEditProfileScreen(editViewModel)
//                DanggnInfoScreen(
//                    modifier = Modifier.fillMaxSize(),
//                    onClickBackButton = { onBackPressed() }
//                )
            }
        }
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, DanggnInfoActivity::class.java).apply {
            putExtra(EXTRA_ANIMATION, NavigationAnimationType.SLIDE)
        }
    }
}
