package com.mashup.ui.setting

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mashup.R
import com.mashup.core.ui.theme.MashUpTheme
import com.mashup.core.common.R as CR

@Composable
fun SettingScreen(
    onLogout: () -> Unit,
    onDeleteUser: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        BasicSettingItem(
            text = stringResource(id = R.string.logout),
            textColorRes = CR.color.gray800,
            onClickItem = onLogout
        )
        RightArrowSettingItem(
            text = stringResource(id = R.string.delete_account),
            textColorRes = CR.color.red500,
            onClickItem = onDeleteUser
        )
    }
}

@Preview("DarkMode")
@Preview
@Composable
fun SettingScreenPrev() {
    MashUpTheme {
        Surface(color = MaterialTheme.colors.onBackground) {
            SettingScreen(
                modifier = Modifier.fillMaxWidth(),
                onLogout = {},
                onDeleteUser = {}
            )
        }
    }
}