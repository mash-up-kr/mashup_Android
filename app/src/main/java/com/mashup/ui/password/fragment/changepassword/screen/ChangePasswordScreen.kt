package com.mashup.ui.password.fragment.changepassword.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mashup.R
import com.mashup.core.common.model.TextFieldInputType
import com.mashup.core.common.model.Validation
import com.mashup.core.ui.colors.Gray50
import com.mashup.core.ui.theme.MashUpTheme
import com.mashup.core.ui.typography.Header1
import com.mashup.core.ui.widget.ButtonStyle
import com.mashup.core.ui.widget.MashUpButton
import com.mashup.core.ui.widget.MashUpTextField
import com.mashup.ui.password.ButtonState
import com.mashup.ui.password.PassWordState
import com.mashup.ui.password.PwdCheckState
import com.mashup.ui.password.PwdState

@Composable
internal fun ChangePasswordScreen(
    inputPassword: String,
    inputPasswordConfirm: String,
    passwordState: PassWordState,
    modifier: Modifier = Modifier,
    onInputPasswordChanged: (String) -> Unit,
    onInputPasswordConfirmChanged: (String) -> Unit,
    onClickDone: () -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .verticalScroll(scrollState)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 20.dp,
                    vertical = 12.dp
                ),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = stringResource(R.string.change_password_hint),
                style = Header1,
                modifier = Modifier.fillMaxWidth()
            )

            MashUpTextField(
                text = inputPassword,
                onTextChanged = onInputPasswordChanged,
                labelText = stringResource(R.string.password),
                requestFocus = false,
                validation = when (passwordState.pwdState) {
                    PwdState.Empty -> Validation.EMPTY
                    PwdState.Success -> Validation.SUCCESS
                    PwdState.Error -> Validation.FAILED
                },
                textFieldInputType = TextFieldInputType.PASSWORD,
                modifier = Modifier.fillMaxWidth(),
                validationText = when (passwordState.pwdState) {
                    PwdState.Error -> stringResource(R.string.change_password_screen_password_validation_error_content)
                    else -> stringResource(R.string.change_password_screen_password_validation_default_content)
                }
            )

            MashUpTextField(
                text = inputPasswordConfirm,
                onTextChanged = onInputPasswordConfirmChanged,
                labelText = stringResource(R.string.password_confirm),
                requestFocus = false,
                validation = when (passwordState.pwdCheckState) {
                    PwdCheckState.Empty -> Validation.EMPTY
                    PwdCheckState.Success -> Validation.SUCCESS
                    PwdCheckState.Error -> Validation.FAILED
                },
                textFieldInputType = TextFieldInputType.PASSWORD,
                modifier = Modifier.fillMaxWidth(),
                validationText = when (passwordState.pwdCheckState) {
                    PwdCheckState.Error -> stringResource(R.string.change_password_screen_password_check_validation_error_content)
                    else -> ""
                },
                enabled = when (passwordState.pwdState) {
                    PwdState.Success -> true
                    else -> false
                }
            )
        }

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )

        MashUpButton(
            text = stringResource(R.string.done),
            buttonStyle = ButtonStyle.PRIMARY,
            isEnabled = when (passwordState.buttonState) {
                ButtonState.Enable -> true
                else -> false
            },
            showLoading = when (passwordState.buttonState) {
                ButtonState.Loading -> true
                else -> false
            },
            onClick = onClickDone,
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        )
    }
}

@Preview
@Composable
private fun PreviewChangePasswordScreen() {
    MashUpTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Gray50)
        ) {
            ChangePasswordScreen(
                inputPassword = "",
                inputPasswordConfirm = "",
                passwordState = PassWordState.Empty,
                onInputPasswordChanged = {},
                onInputPasswordConfirmChanged = {},
                onClickDone = {}
            )
        }
    }
}
