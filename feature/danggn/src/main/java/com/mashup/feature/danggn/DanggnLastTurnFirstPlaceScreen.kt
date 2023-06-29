package com.mashup.feature.danggn

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mashup.core.ui.theme.MashUpTheme
import com.mashup.core.ui.typography.Title1
import com.mashup.core.ui.widget.MashUpButton

@Composable
fun DanggnLastTurnFirstPlaceScreen(
    turn: Int,
    name: String,
    modifier: Modifier = Modifier,
    onClickCloseButton: () -> Unit = {},
) {
    Box(modifier = modifier) {
        DanggnLastTurnFirstPlaceContent(
            name = name,
            turn = turn,
            modifier = modifier,
            onClickCloseButton = onClickCloseButton
        )
        DanggnKonfettiView(modifier = Modifier.fillMaxSize())
    }
}

@Composable
private fun DanggnLastTurnFirstPlaceContent(
    turn: Int,
    name: String,
    modifier: Modifier = Modifier,
    onClickCloseButton: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .background(color = Color(0xB3000000))
            .fillMaxSize()
            .padding(horizontal = 38.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            modifier = Modifier,
            painter = painterResource(id = R.drawable.img_carrot_rank_in),
            contentDescription = null
        )

        Spacer(modifier = Modifier.height(26.dp))

        Text(
            modifier = Modifier,
            text = "축하드려요!\n" +
                    "이번 ${turn}회차\n" +
                    "${name}님이 1등을 차지했어요!",
            textAlign = TextAlign.Center,
            color = Color.White,
            style = Title1
        )

        Spacer(modifier = Modifier.height(25.dp))

        MashUpButton(
            modifier = Modifier
                .width(140.dp)
                .height(52.dp),
            text = "리워드 확인하기", onClick = onClickCloseButton
        )
    }
}

@Composable
@Preview
fun PreviewDanggnLastTurnFirstPlace() {
    MashUpTheme {
        Surface(color = Color.White) {
            DanggnLastTurnFirstPlaceScreen(
                turn = 1,
                name = "매숑이"
            )
        }
    }
}
