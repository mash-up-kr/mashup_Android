package com.mashup.feature.danggn

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.mashup.core.ui.theme.MashUpTheme
import com.mashup.core.ui.typography.Title1
import com.mashup.core.ui.widget.MashUpButton
import nl.dionsegijn.konfetti.compose.KonfettiView
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.Rotation
import nl.dionsegijn.konfetti.core.emitter.Emitter
import nl.dionsegijn.konfetti.core.models.Shape
import nl.dionsegijn.konfetti.core.models.Size
import java.util.concurrent.TimeUnit
import com.mashup.core.common.R as CR

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
    Box(modifier = modifier) {
        DanggnFirstPlaceContent(
            modifier = Modifier.fillMaxSize(),
            name = name,
            onClickCloseButton = onClickCloseButton
        )
        DanggnKonfettiView(modifier = Modifier.fillMaxSize())
    }
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
fun DanggnKonfettiView(
    modifier: Modifier = Modifier
) {
    val drawable = ContextCompat.getDrawable(
        LocalContext.current,
        CR.drawable.img_carrot
    )
    val defaultParty = Party(
        speed = 0f,
        maxSpeed = 20f,
        damping = 0.9f,
        spread = 360,
        size = listOf(Size.MEDIUM.copy(sizeInDp = 36), Size.LARGE.copy(sizeInDp = 64)),
        shapes = listOf(
            drawable?.let { Shape.DrawableShape(it, tint = false) } ?: Shape.Square
        ),
        rotation = Rotation(enabled = false),
        emitter = Emitter(duration = 100, TimeUnit.MILLISECONDS).max(30),
        position = Position.Relative(0.25, 0.2)
    )

    KonfettiView(
        modifier = modifier,
        parties = listOf(
            defaultParty,
            defaultParty.copy(
                position = Position.Relative(0.75, 0.2)
            ),
            defaultParty.copy(
                position = Position.Relative(0.5, 0.75)
            )
        )
    )
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