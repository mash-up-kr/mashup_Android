package com.mashup.feature.danggn

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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

/**
 * 개인일 경우 {username}님
 * 플랫폼일 경우 {platforrm}팀
 * 까지 받아야합니다!!!!!!!! 나머지 텍스트가 공통입니다
 * [figma] https://www.figma.com/file/kxgTs6r19oJz6ipQGYm83d/%EB%A7%A4%EC%89%AC%EC%97%85-%EC%95%B1?node-id=504-29106&t=ExMNilr25jrPhyWt-0
 */
@Composable
fun DanggnFirstPlaceScreen(
    name: String,
    modifier: Modifier = Modifier,
    onClickCloseButton: () -> Unit = {},
) {
    DanggnFirstPlaceContent(
        modifier = modifier,
        name = name,
        onClickCloseButton = onClickCloseButton
    )
}

@Composable
private fun DanggnFirstPlaceContent(
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
            text = "축하드려요!\n${name}이 1등을 차지했어요!",
            textAlign = TextAlign.Center,
            color = Color.White,
            style = Title1
        )

        Spacer(modifier = Modifier.height(25.dp))

        MashUpButton(
            modifier = Modifier
                .width(120.dp)
                .height(52.dp),
            text = "확인", onClick = onClickCloseButton
        )
    }
}

@Composable
@Preview
fun PreviewDanggnFirstPlace() {
    MashUpTheme {
        Surface(color = Color.White) {
            DanggnFirstPlaceScreen(
                name = "매숑이님"
            )
        }
    }
}