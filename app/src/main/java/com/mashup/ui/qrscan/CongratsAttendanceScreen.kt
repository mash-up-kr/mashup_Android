package com.mashup.ui.qrscan

import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mashup.R
import com.mashup.compose.colors.Black
import com.mashup.compose.theme.MashUpTheme

@Composable
fun CongratsAttendanceScreen(
    isVisible: Boolean
) {
    AnimatedVisibility(
        visible = isVisible,
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Black.copy(alpha = 0.8f))
                    .animateEnterExit(
                        enter = fadeIn(),
                        exit = fadeOut()
                    )
            )
            CongratsImage(
                modifier = Modifier
                    .fillMaxSize()
            )
        }
    }
}

@Composable
fun AnimatedVisibilityScope.CongratsImage(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .animateEnterExit(
                enter = scaleIn(),
                exit = scaleOut()
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            modifier = Modifier.size(240.dp, 180.dp),
            painter = painterResource(id = R.drawable.img_success_attendance),
            contentDescription = null
        )
    }
}

@Preview
@Composable
fun AttendanceSuccessScreenPrev() {
    MashUpTheme {
        CongratsAttendanceScreen(true)
    }
}