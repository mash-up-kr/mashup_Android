package com.mashup.ui.setting.menu

import android.content.res.Configuration.UI_MODE_NIGHT_YES
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
fun SettingMenuList(
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

@Preview(name = "DarkMode", uiMode = UI_MODE_NIGHT_YES)
@Preview
@Composable
fun SettingMenuListPrev() {
    MashUpTheme {
        Surface(color = MaterialTheme.colors.onBackground) {
            SettingMenuList(
                modifier = Modifier.fillMaxWidth(),
                onLogout = {},
                onDeleteUser = {}
            )
        }
    }
}