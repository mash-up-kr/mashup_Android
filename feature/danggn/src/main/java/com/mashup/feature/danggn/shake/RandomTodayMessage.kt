package com.mashup.feature.danggn.shake

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mashup.core.ui.colors.Gray100
import com.mashup.core.ui.colors.Gray600
import com.mashup.core.ui.theme.MashUpTheme
import com.mashup.core.ui.typography.Body4

@Composable
fun RandomTodayMessage(modifier: Modifier, message: String) {
    Surface(
        modifier = modifier,
        color = Gray100,
        shape = CircleShape
    ) {
        Text(
            text = message,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            style = Body4,
            color = Gray600
        )
    }
}

@Composable
@Preview
fun DanggnRandomTodayMessagePrev() {
    MashUpTheme {
        RandomTodayMessage(modifier = Modifier, message = "힘들면 당근 흔들어잇!")
    }
}
