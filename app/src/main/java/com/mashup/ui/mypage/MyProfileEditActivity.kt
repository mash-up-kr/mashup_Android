package com.mashup.ui.mypage

import android.content.Context
import android.content.Intent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.ui.Modifier
import com.mashup.base.BaseViewBindingActivity
import com.mashup.core.common.utils.ToastUtil
import com.mashup.core.ui.theme.MashUpTheme
import com.mashup.databinding.ActivityMyProfileEditBinding
import com.mashup.feature.mypage.profile.MyPageProfileEditViewModel
import com.mashup.feature.mypage.profile.edit.MyPageEditProfileScreen
import com.mashup.feature.mypage.profile.model.LoadState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class MyProfileEditActivity : BaseViewBindingActivity<ActivityMyProfileEditBinding>() {
    override val viewBinding by lazy { ActivityMyProfileEditBinding.inflate(layoutInflater) }
    private val editViewModel: MyPageProfileEditViewModel by viewModels()

    override fun initViews() {
        super.initViews()

        viewBinding.viewCompose.setContent {
            MashUpTheme {
                MyPageEditProfileScreen(
                    viewModel = editViewModel,
                    onBackPressed = ::finish,
                    modifier = Modifier
                        .statusBarsPadding()
                        .navigationBarsPadding()
                )
            }
        }

        setObserver()
    }

    private fun setObserver() {
        flowLifecycleScope {
            editViewModel.profileCardState.collectLatest {
                if (it is LoadState.Loaded) {
                    ToastUtil.showToast(this@MyProfileEditActivity, "저장 완료!")
                    setResult(RESULT_OK)
                }
            }
        }

        flowLifecycleScope {
            editViewModel.invalidInputError.collectLatest { errorMessage ->
                ToastUtil.showToast(this@MyProfileEditActivity, errorMessage)
            }
        }
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, MyProfileEditActivity::class.java).apply {
            }
        }
    }
}
