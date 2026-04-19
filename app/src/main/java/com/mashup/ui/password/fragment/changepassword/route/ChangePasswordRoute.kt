package com.mashup.ui.password.fragment.changepassword.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mashup.ui.password.PasswordViewModel
import com.mashup.ui.password.ScreenState
import com.mashup.ui.password.fragment.changepassword.screen.ChangePasswordScreen

@Composable
internal fun ChangePasswordRoute(
    viewModel: PasswordViewModel,
    modifier: Modifier = Modifier
) {
    val passwordState by viewModel.passwordScreenState.collectAsStateWithLifecycle()
    val password by viewModel.pwd.collectAsStateWithLifecycle()
    val passwordCheck by viewModel.pwdCheck.collectAsStateWithLifecycle()

    ChangePasswordScreen(
        inputPassword = password,
        inputPasswordConfirm = passwordCheck,
        passwordState = passwordState,
        modifier = modifier,
        onInputPasswordChanged = viewModel::updatePassword,
        onInputPasswordConfirmChanged = viewModel::updatePasswordCheck,
        onClickDone = {
            viewModel.onClickNextButton(ScreenState.ChangePassword)
        }
    )
}
