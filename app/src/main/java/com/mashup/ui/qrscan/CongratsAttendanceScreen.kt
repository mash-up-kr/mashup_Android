package com.mashup.ui.qrscan

import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mashup.R
import com.mashup.compose.colors.Black
import com.mashup.compose.colors.White
import com.mashup.compose.typography.MashTextView

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
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.img_sucess_attendance),
            contentDescription = null
        )

        MashTextView(
            modifier = Modifier.padding(top = 40.dp),
            text = "1부 출석 완료!",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = White
        )
    }
}