package com.mashup.ui.qrscan

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mashup.core.common.R
import com.mashup.core.ui.theme.MashUpTheme

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun CongratsAttendanceScreen(
    isVisible: Boolean
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn() + scaleIn(),
        exit = fadeOut()
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            CongratsImage(
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Composable
fun CongratsImage(modifier: Modifier = Modifier) {
    Image(
        modifier = modifier.size(240.dp, 180.dp),
        painter = painterResource(id = R.drawable.img_success_attendance),
        contentDescription = null
    )
}

@Preview
@Composable
fun AttendanceSuccessScreenPrev() {
    MashUpTheme {
        CongratsAttendanceScreen(true)
    }
}
