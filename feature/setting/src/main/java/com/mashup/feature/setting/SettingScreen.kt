package com.mashup.feature.setting

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mashup.core.ui.theme.MashUpTheme
import com.mashup.core.ui.widget.MashUpToolbar
import com.mashup.feature.setting.ui.menu.SettingMenuList
import com.mashup.feature.setting.ui.sns.SNSList

@ExperimentalFoundationApi
@Composable
fun SettingScreen(
    modifier: Modifier = Modifier,
    onClickPush: () -> Unit = {},
    onClickBackButton: () -> Unit = {},
    onLogout: () -> Unit = {},
    onDeleteUser: () -> Unit = {},
    onClickSNS: (String) -> Unit = {}
) {
    Column(
        modifier = modifier
    ) {
        MashUpToolbar(
            title = "설정",
            showBackButton = true,
            onClickBackButton = onClickBackButton
        )

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            SettingMenuList(
                modifier = Modifier.fillMaxWidth(),
                onLogout = onLogout,
                onDeleteUser = onDeleteUser,
                onClickPush = onClickPush
            )

            SNSList(
                modifier = Modifier.fillMaxWidth(),
                onClickSNS = onClickSNS
            )
        }
    }
}

@ExperimentalFoundationApi
@Preview(name = "DarkMode", uiMode = UI_MODE_NIGHT_YES)
@Preview
@Composable
fun SettingScreenPrev() {
    MashUpTheme {
        Surface(color = MaterialTheme.colors.onBackground) {
            SettingScreen(
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
