package com.mashup.ui.password.fragment.changepassword

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.fragment.app.activityViewModels
import com.mashup.R
import com.mashup.base.BaseComposeFragment
import com.mashup.core.common.utils.ToastUtil
import com.mashup.ui.password.PasswordViewModel
import com.mashup.ui.password.fragment.changepassword.route.ChangePasswordRoute
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class ChangePasswordFragment : BaseComposeFragment() {

    private val passwordViewModel: PasswordViewModel by activityViewModels()

    override fun initObserves() {
        super.initObserves()

        flowViewLifecycleScope {
            passwordViewModel.moveToNextScreen.collectLatest {
                val activity = this@ChangePasswordFragment.requireActivity()

                ToastUtil.showToast(
                    context = activity,
                    message = getString(R.string.change_password_screen_success_toast_message)
                )

                activity.finish()
            }
        }
    }

    @Composable
    override fun MainContainer(modifier: Modifier) {
        ChangePasswordRoute(
            viewModel = passwordViewModel
        )
    }
}
