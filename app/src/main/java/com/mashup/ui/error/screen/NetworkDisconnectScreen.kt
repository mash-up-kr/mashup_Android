package com.mashup.ui.error.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mashup.core.common.R
import com.mashup.core.ui.colors.Gray200
import com.mashup.core.ui.theme.MashUpTheme
import com.mashup.core.ui.typography.Special
import com.mashup.core.ui.widget.MashUpButton

@Composable
internal fun NetworkDisconnectScreen(
    showLoading: Boolean,
    modifier: Modifier = Modifier,
    onClickRetry: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize()
            .background(Color.White)
            .then(modifier)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
                .padding(
                    top = 8.dp,
                    end = 8.dp
                ),
            contentAlignment = Alignment.CenterEnd
        ) {
            IconButton(
                onClick = onClickRetry,
                modifier = Modifier
                    .size(
                        width = 40.dp,
                        height = 40.dp
                    )
                    .padding(8.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_close),
                    contentDescription = "close",
                )
            }
        }

        Column(
            modifier = Modifier.fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(
                modifier = Modifier.fillMaxWidth()
                    .weight(0.4f)
            )

            Text(
                text = stringResource(com.mashup.R.string.network_disconnect_screen_content),
                color = Gray200,
                style = Special,
            )

            Spacer(
                modifier = Modifier.fillMaxWidth()
                    .height(40.dp)
            )

            MashUpButton(
                text = stringResource(com.mashup.R.string.network_disconnect_screen_button_content),
                onClick = onClickRetry,
                showLoading = showLoading
            )

            Spacer(
                modifier = Modifier.fillMaxWidth()
                    .weight(0.6f)
            )
        }
    }
}

@Preview
@Composable
private fun PreviewNetworkDisconnectScreen() {
    MashUpTheme {
        NetworkDisconnectScreen(
            showLoading = false,
            modifier = Modifier.fillMaxSize(),
            onClickRetry = {}
        )
    }
}
