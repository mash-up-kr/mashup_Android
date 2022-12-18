package com.mashup.ui.setting.sns

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mashup.core.ui.theme.MashUpTheme
import com.mashup.core.ui.typography.Body3
import com.mashup.core.common.R as CR

@Composable
fun SNSItem(
    name: String,
    @DrawableRes snsImageRes: Int,
    onClickItem: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        backgroundColor = Color.White,
        elevation = 2.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = modifier
                .height(78.dp)
                .clickable(onClick = onClickItem)
        ) {
            Image(
                painter = painterResource(id = snsImageRes),
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
                snsImageRes = CR.drawable.ic_facebook,
                onClickItem = { }
            )
        }
    }
}
