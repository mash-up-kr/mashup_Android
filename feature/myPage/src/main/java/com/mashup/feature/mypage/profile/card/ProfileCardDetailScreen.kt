package com.mashup.feature.mypage.profile.card

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mashup.core.model.Platform
import com.mashup.core.ui.colors.Gray50
import com.mashup.core.ui.theme.MashUpTheme
import com.mashup.core.ui.widget.ButtonStyle
import com.mashup.core.ui.widget.MashUpButton
import com.mashup.core.ui.widget.MashUpToolbar

@Composable
fun ProfileCardDetailScreen() {
}

@Composable
fun ProfileCardDetailContent(
    generationNumber: Int,
    name: String,
    platform: Platform,
    isRunning: Boolean,
    modifier: Modifier = Modifier,
    team: String = "",
    staff: String = "",
    onBackPressed: () -> Unit = {},
    onDownLoadClicked: () -> Unit = {},
    onEditClicked: () -> Unit = {}
) {
    Column(
        modifier = modifier
    ) {
        MashUpToolbar(
            modifier = Modifier.fillMaxWidth(),
            title = "활동 카드",
            showBackButton = true,
            onClickBackButton = onBackPressed
        )
        Column(
            modifier = Modifier
                .weight(1f),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(modifier = Modifier.fillMaxWidth().weight(1f)) {
                ProfileCard(
                    modifier = Modifier.align(Alignment.Center),
                    generationNumber = generationNumber,
                    name = name,
                    platform = platform,
                    isRunning = isRunning,
                    team = team,
                    staff = staff,
                    onClick = {}
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 28.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                MashUpButton(
                    modifier = Modifier.weight(1f),
                    text = "다운로드",
                    buttonStyle = ButtonStyle.DARK,
                    onClick = onDownLoadClicked
                )

                MashUpButton(
                    modifier = Modifier.weight(1f),
                    text = "편집",
                    buttonStyle = ButtonStyle.PRIMARY,
                    onClick = onEditClicked
                )
            }
        }
    }
}

@Preview
@Composable
fun ProfileCardDetailContentPrev() {
    MashUpTheme {
        Surface(
            color = Gray50
        ) {
            ProfileCardDetailContent(
                modifier = Modifier.fillMaxSize(),
                generationNumber = 12,
                name = "김매숑",
                platform = Platform.DESIGN,
                isRunning = false
            )
        }
    }
}
