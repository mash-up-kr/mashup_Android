package com.mashup.ui.mypage

import android.content.Context
import android.content.Intent
import androidx.activity.viewModels
import com.mashup.R
import com.mashup.base.BaseActivity
import com.mashup.core.ui.theme.MashUpTheme
import com.mashup.databinding.ActivityMyProfileEditBinding
import com.mashup.feature.mypage.profile.MyPageProfileEditViewModel
import com.mashup.feature.mypage.profile.edit.MyPageEditProfileScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyProfileEditActivity : BaseActivity<ActivityMyProfileEditBinding>() {
    override val layoutId = R.layout.activity_my_profile_edit
    private val editViewModel: MyPageProfileEditViewModel by viewModels()

    override fun initViews() {
        super.initViews()
        viewBinding.viewCompose.setContent {
            MashUpTheme {
                MyPageEditProfileScreen(editViewModel)
            }
        }
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, MyProfileEditActivity::class.java)
        }
    }
}