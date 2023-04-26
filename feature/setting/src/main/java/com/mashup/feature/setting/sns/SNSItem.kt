package com.mashup.feature.setting.sns

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mashup.core.ui.extenstions.shadow
import com.mashup.core.ui.theme.MashUpTheme
import com.mashup.core.ui.typography.Body3
import com.mashup.core.common.R as CR

@Composable
fun SNSItem(
    name: String,
    @DrawableRes snsIconRes: Int,
    onClickItem: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = Modifier
            .padding(4.dp)
            .shadow(
                Color(0f, 0f, 0f, 0.05f), // #000000 + alpha 5%
                borderRadius = 12.dp,
                offsetY = 2.dp,
                blurRadius = 4.dp
            )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = modifier
                .fillMaxWidth()
                .height(78.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White)
                .clickable(onClick = onClickItem)
        ) {
            Image(
                painter = painterResource(id = snsIconRes),
                contentDescription = "$name icon",
                modifier = Modifier.size(24.dp)
            )

            Text(
                text = name,
                style = Body3,
                color = colorResource(CR.color.gray700),
                modifier = Modifier.padding(top = 5.dp)
            )
        }
    }
}

@Preview(widthDp = 156)
@Composable
fun SNSItemPrev() {
    MashUpTheme {
        Surface(color = MaterialTheme.colors.onBackground) {
            SNSItem(
                name = "FaceBook",
                snsIconRes = CR.drawable.ic_facebook,
                onClickItem = { }
            )
        }
    }
}
