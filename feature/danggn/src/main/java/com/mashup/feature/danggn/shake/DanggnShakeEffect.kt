package com.mashup.feature.danggn.shake

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mashup.feature.danggn.data.danggn.DanggnMode
import com.mashup.feature.danggn.data.danggn.DanggnScoreModel
import com.mashup.feature.danggn.data.danggn.GoldenDanggnMode
import com.mashup.feature.danggn.data.danggn.NormalDanggnMode
import kotlin.random.Random
import kotlinx.coroutines.delay
import com.mashup.core.common.R as CR

@Composable
fun DanggnShakeEffect(
    modifier: Modifier = Modifier,
    danggnMode: DanggnMode,
    effectList: List<DanggnScoreModel> = emptyList(),
) {
    Box(
        modifier = modifier.background(if (danggnMode is GoldenDanggnMode) Color(0xCC000000) else Color.Transparent)
    ) {
        if (danggnMode is GoldenDanggnMode) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 50.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = CR.drawable.img_fevertime_title),
                    contentDescription = null,
                    modifier = Modifier.width(300.dp)
                )

                val countDown = danggnMode.remainTimeInSeconds.toInt()
                val countDownDrawableRes = remember(countDown) {
                    when(countDown) {
                        1 -> CR.drawable.img_fevertime_countdown_1
                        2 -> CR.drawable.img_fevertime_countdown_2
                        3 -> CR.drawable.img_fevertime_countdown_3
                        else -> null
                    }
                }

                if (countDownDrawableRes != null) {
                    Image(
                        painter = painterResource(id = countDownDrawableRes),
                        contentDescription = null,
                        modifier = Modifier.size(80.dp)
                    )
                }
            }

            Image(
                painter = painterResource(id = CR.drawable.img_fever_danggn),
                contentDescription = null,
                modifier = Modifier
                    .size(174.dp)
                    .align(Alignment.Center)
            )
        }

        effectList.forEach {
            key(it.initTimeMillis) {
                ShakeEffect(danggnMode)
            }
        }
    }
}

@Composable
fun ShakeEffect(danggnMode: DanggnMode) {
    val configuration = LocalConfiguration.current

    val danggnSize = 60.dp
    var isVisible by remember { mutableStateOf(true) }
    val randomX by remember { mutableStateOf(Random.nextInt(configuration.screenWidthDp - danggnSize.value.toInt())) }
    val randomY by remember { mutableStateOf(Random.nextInt(configuration.screenHeightDp - danggnSize.value.toInt())) }

    val fadeOutYPosition by animateDpAsState(
        targetValue = if (isVisible) randomY.dp else (randomY - 30).dp,
        animationSpec = tween(
            durationMillis = 1000,
            easing = LinearOutSlowInEasing
        )
    )

    LaunchedEffect(Unit) {
        delay(100)
        isVisible = false
    }

    Box(modifier = Modifier.offset(x = randomX.dp, y = fadeOutYPosition)) {
        AnimatedVisibility(
            visible = isVisible,
            exit = fadeOut(
                targetAlpha = 0.0f,
                animationSpec = tween(
                    durationMillis = 1000
                )
            ),
        ) {
            Image(
                painterResource(id = if (danggnMode is NormalDanggnMode) CR.drawable.img_carrot else CR.drawable.img_fever_danggn),
                contentDescription = null,
                modifier = Modifier.size(danggnSize),
            )
        }
    }
}


@Preview
@Composable
fun NormalDanggnModeEffectPrev() {
    DanggnShakeEffect(
        modifier = Modifier.fillMaxSize(),
        danggnMode = NormalDanggnMode,
    )
}

@Preview
@Composable
fun GoldenDanggnModeEffectPrev() {
    DanggnShakeEffect(
        modifier = Modifier.fillMaxSize(),
        danggnMode = GoldenDanggnMode(),
    )
}
