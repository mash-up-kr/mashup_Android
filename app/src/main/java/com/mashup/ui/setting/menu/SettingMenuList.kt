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
import com.mashup.core.model.data.local.UserPreference
import com.mashup.core.ui.theme.MashUpTheme
import com.mashup.core.common.R as CR

@Composable
fun SettingMenuList(
    userPreference: UserPreference,
    onToggleFcm: (Boolean) -> Unit,
    onLogout: () -> Unit,
    onDeleteUser: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        FcmToggleSettingItem(
            title = stringResource(id = R.string.mash_up_alarm_title),
            titleColorRes = CR.color.black,
            description = stringResource(id = R.string.mash_up_alarm_description),
            descriptionRes = CR.color.gray500,
            onCheckedChange = onToggleFcm,
            checked = userPreference.pushNotificationAgreed
        )
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
@Composable
fun SettingMenuListPrev() {
    MashUpTheme {
        Surface(color = MaterialTheme.colors.onBackground) {
            SettingMenuList(
                modifier = Modifier.fillMaxWidth(),
                onLogout = {},
                onDeleteUser = {},
                onToggleFcm = {},
                userPreference = UserPreference.getDefaultInstance()
            )
        }
    }
}
