package com.mashup.feature.danggn.shake

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.mashup.feature.danggn.data.danggn.DanggnMode
import com.mashup.feature.danggn.data.danggn.NormalDanggnMode
import com.mashup.core.common.R as CR

@Composable
fun DanggnShakeContent(
    randomTodayMessage: String,
    danggnMode: DanggnMode,
    modifier: Modifier = Modifier,
) {

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(400.dp)
    ) {
        if (danggnMode is NormalDanggnMode) {
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
