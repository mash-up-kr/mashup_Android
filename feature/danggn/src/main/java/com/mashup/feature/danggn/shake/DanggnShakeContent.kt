package com.mashup.feature.danggn.shake

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.mashup.feature.danggn.DanggnViewModel
import com.mashup.feature.danggn.data.danggn.NormalDanggnMode
import com.mashup.core.common.R as CR

@Composable
fun DanggnShakeContent(
    modifier: Modifier = Modifier,
    viewModel: DanggnViewModel,
) {
    val randomTodayMessage = viewModel.randomMessage.collectAsState().value
    val danggnMode = viewModel.danggnMode.collectAsState()

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(400.dp)
    ) {
        if (danggnMode.value is NormalDanggnMode) {
            Image(
                painter = painterResource(id = CR.drawable.img_carrot),
                contentDescription = null,
                modifier = Modifier
                    .padding(bottom = 50.dp)
                    .size(150.dp)
                    .align(Alignment.Center)
            )
        }

        // 랜덤 오늘의 하소연
        RandomTodayMessage(
            modifier = Modifier
                .padding(bottom = 12.dp)
                .align(Alignment.BottomCenter),
            message = randomTodayMessage
        )
    }
}
