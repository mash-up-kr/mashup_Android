package com.mashup.ui.attendance.platform

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mashup.R
import com.mashup.compose.colors.Gray800
import com.mashup.compose.theme.MashUpTheme
import com.mashup.compose.typography.Body3
import com.mashup.compose.typography.MashTextView

@Composable
fun AttendanceNoticeItem(
    notice: String,
    modifier: Modifier = Modifier
) {
    Card(modifier = Modifier.shadow(elevation = 2.dp)) {
        Row(modifier = modifier) {
            Image(
                modifier = Modifier
                    .padding(start = 10.dp)
                    .padding(vertical = 6.dp),
                painter = painterResource(id = R.drawable.img_notice),
                contentDescription = null
            )

            MashTextView(
                modifier = modifier
                    .padding(start = 6.dp, end = 18.dp)
                    .align(CenterVertically),
                text = notice,
                style = Body3,
                color = Gray800
            )
        }
    }
}

@Preview
@Composable
fun AttendanceNoticeItemPrev() {
    MashUpTheme {
        AttendanceNoticeItem(notice = "공지사항")
    }
}