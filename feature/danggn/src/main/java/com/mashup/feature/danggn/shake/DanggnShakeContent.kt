package com.mashup.feature.danggn.shake

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mashup.feature.danggn.DanggnViewModel

@Composable
fun DanggnShakeContent(
    modifier: Modifier = Modifier,
    viewModel: DanggnViewModel,
) {
    val randomTodayMessage = viewModel.randomMessage.collectAsState().value

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(400.dp)
    ) {
        // 랜덤 오늘의 하소연
        RandomTodayMessage(
            modifier = Modifier
                .padding(bottom = 12.dp)
                .align(Alignment.BottomCenter),
            message = randomTodayMessage
        )
    }
}
