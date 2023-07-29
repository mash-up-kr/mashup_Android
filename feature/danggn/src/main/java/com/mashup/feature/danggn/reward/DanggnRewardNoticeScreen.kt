package com.mashup.feature.danggn.reward

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mashup.core.ui.colors.Gray500
import com.mashup.core.ui.colors.Gray900
import com.mashup.core.ui.extenstions.noRippleClickable
import com.mashup.core.ui.theme.MashUpTheme
import com.mashup.core.ui.typography.Header1
import com.mashup.core.ui.typography.SubTitle1
import com.mashup.feature.danggn.DanggnKonfettiView

@Composable
fun DanggnRewardNoticeScreen(
    name: String,
    message: String,
    modifier: Modifier = Modifier,
    onClickCloseButton: () -> Unit = {},
) {
    Box(modifier = modifier) {
        DanggnRewardNoticeContent(
            name = name,
            message = message,
            modifier = Modifier.fillMaxSize(),
            onClickCloseButton = onClickCloseButton
        )
        DanggnKonfettiView(modifier = Modifier.fillMaxSize())
    }
}

@Composable
private fun DanggnRewardNoticeContent(
    name: String,
    message: String,
    modifier: Modifier = Modifier,
    onClickCloseButton: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .background(color = Color(0xB3000000))
            .padding(horizontal = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Column(
            modifier = Modifier
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(23.dp)
                )
                .padding(horizontal = 40.dp)
                .padding(top = 40.dp, bottom = 50.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "From.$name",
                color = Gray500,
                style = SubTitle1
            )

            Text(
                modifier = Modifier.padding(top = 40.dp),
                text = message,
                color = Gray900,
                textAlign = TextAlign.Center,
                style = Header1
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Box(
            modifier = Modifier
                .background(color = Color.White, shape = CircleShape)
                .noRippleClickable(onClick = onClickCloseButton)
                .padding(10.dp)
        ) {
            Icon(
                modifier = Modifier.size(20.dp),
                painter = painterResource(com.mashup.core.common.R.drawable.ic_close),
                contentDescription = null
            )
        }
    }
}

@Composable
@Preview
fun PreviewDanggnLastRoundFirstPlace() {
    MashUpTheme {
        DanggnRewardNoticeScreen(
            modifier = Modifier.fillMaxSize(),
            name = "김당근",
            message = "금잔디\n오늘부터 너는\n내 여자친구다"
        )
    }
}
