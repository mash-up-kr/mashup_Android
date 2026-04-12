package com.mashup.ui.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mashup.R
import com.mashup.core.common.model.TextFieldInputType
import com.mashup.core.common.model.Validation
import com.mashup.core.ui.colors.Gray200
import com.mashup.core.ui.colors.Gray600
import com.mashup.core.ui.extenstions.noRippleClickable
import com.mashup.core.ui.theme.MashUpTheme
import com.mashup.core.ui.typography.Body2
import com.mashup.core.ui.widget.ButtonStyle
import com.mashup.core.ui.widget.MashUpButton
import com.mashup.core.ui.widget.MashUpTextField

@Composable
fun LoginScreen(
    id: String,
    password: String,
    loginState: LoginState,
    validation: Validation,
    setId: (id: String) -> Unit,
    setPassword: (pwd: String) -> Unit,
    onClickLogin: (id: String, pwd: String) -> Unit,
    onClickSignUp: () -> Unit,
    onClickChangePassword: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Spacer(modifier = Modifier.height(40.dp))

        MashUpTextField(
            text = id,
            onTextChanged = setId,
            labelText = stringResource(R.string.id),
            requestFocus = false,
            validation = Validation.NONE,
            textFieldInputType = TextFieldInputType.TEXT,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        MashUpTextField(
            text = password,
            onTextChanged = setPassword,
            labelText = stringResource(R.string.password),
            requestFocus = false,
            validation = Validation.NONE,
            textFieldInputType = TextFieldInputType.PASSWORD,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        MashUpButton(
            text = stringResource(R.string.login),
            buttonStyle = ButtonStyle.PRIMARY,
            isEnabled = validation == Validation.SUCCESS,
            showLoading = loginState == LoginState.Loading,
            onClick = { onClickLogin(id, password) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        ) {
            Text(
                text = stringResource(R.string.sign_up),
                color = Gray600,
                style = Body2,
                modifier = Modifier.noRippleClickable(onClickSignUp)
            )
            Spacer(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .width(1.dp)
                    .fillMaxHeight()
                    .background(color = Gray200)
                    .padding(vertical = 3.5.dp)
            )
            Text(
                text = stringResource(R.string.change_password),
                color = Gray600,
                style = Body2,
                modifier = Modifier.noRippleClickable(onClickChangePassword)
            )
        }
    }
}

@Preview
@Composable
private fun LoginScreenPreview() {
    MashUpTheme {
        LoginScreen(
            id = "",
            password = "",
            loginState = LoginState.Empty,
            validation = Validation.NONE,
            setId = {},
            setPassword = {},
            onClickLogin = { _, _ -> },
            onClickSignUp = {},
            onClickChangePassword = {},
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.onBackground)
        )
    }
}
