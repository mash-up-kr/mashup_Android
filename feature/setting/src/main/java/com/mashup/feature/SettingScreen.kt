package com.mashup.feature

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mashup.core.model.data.local.UserPreference
import com.mashup.core.ui.theme.MashUpTheme
import com.mashup.feature.menu.SettingMenuList
import com.mashup.feature.sns.SNSList

@Composable
fun SettingScreen(
    modifier: Modifier = Modifier,
    userPreference: UserPreference,
    onLogout: () -> Unit,
    onDeleteUser: () -> Unit,
    onToggleFcm: (Boolean) -> Unit,
    onClickSNS: (String) -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        SettingMenuList(
            modifier = Modifier.fillMaxWidth(),
            onLogout = onLogout,
            onDeleteUser = onDeleteUser,
            onToggleFcm = onToggleFcm,
            userPreference = userPreference
        )

        SNSList(
            modifier = Modifier.fillMaxWidth(),
            onClickSNS = onClickSNS
        )
    }
}

@Preview(name = "DarkMode", uiMode = UI_MODE_NIGHT_YES)
@Preview
@Composable
fun SettingScreenPrev() {
    MashUpTheme {
        Surface(color = MaterialTheme.colors.onBackground) {
            SettingScreen(
                modifier = Modifier.fillMaxSize(),
                onLogout = {},
                onDeleteUser = {},
                onToggleFcm = {},
                onClickSNS = {},
                userPreference = UserPreference.getDefaultInstance()
            )
        }
    }
}
