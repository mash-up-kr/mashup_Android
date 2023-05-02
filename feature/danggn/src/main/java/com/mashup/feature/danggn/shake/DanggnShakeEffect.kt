package com.mashup.feature.danggn.shake

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mashup.feature.danggn.data.danggn.DanggnMode
import com.mashup.feature.danggn.data.danggn.GoldenDanggnMode
import com.mashup.feature.danggn.data.danggn.NormalDanggnMode
import com.mashup.core.common.R as CR

@Composable
fun DanggnShakeEffect(
    modifier: Modifier = Modifier,
    danggnMode: DanggnMode,
    countDown: Int,
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

                when (countDown) {
                    1 -> Image(
                        painter = painterResource(id = CR.drawable.img_fevertime_countdown_1),
                        contentDescription = null,
                        modifier = Modifier.size(80.dp)
                    )
                    2 -> Image(
                        painter = painterResource(id = CR.drawable.img_fevertime_countdown_2),
                        contentDescription = null,
                        modifier = Modifier.size(80.dp)
                    )
                    3 -> Image(
                        painter = painterResource(id = CR.drawable.img_fevertime_countdown_3),
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
    }
}

@Preview
@Composable
fun NormalDanggnModeEffectPrev() {
    DanggnShakeEffect(
        modifier = Modifier.fillMaxSize(),
        danggnMode = NormalDanggnMode,
        countDown = 0,
    )
}

@Preview
@Composable
fun GoldenDanggnModeEffectPrev() {
    DanggnShakeEffect(
        modifier = Modifier.fillMaxSize(),
        danggnMode = GoldenDanggnMode,
        countDown = 3,
    )
}
