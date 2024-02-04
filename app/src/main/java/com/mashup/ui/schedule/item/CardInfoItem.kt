package com.mashup.ui.schedule.item

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.mashup.core.common.R
import com.mashup.core.ui.colors.Gray100
import com.mashup.core.ui.colors.Gray300
import com.mashup.core.ui.colors.Gray400
import com.mashup.core.ui.colors.Gray600
import com.mashup.core.ui.colors.Gray700
import com.mashup.core.ui.colors.Gray900
import com.mashup.core.ui.typography.Body3
import com.mashup.core.ui.typography.Header1

@Composable
fun CardInfoItem(
    dDay: String,
    title: String,
    calendar: String,
    timeLine: String,
    location: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Box(
            modifier = Modifier.background(
                color = Gray100,
                shape = RoundedCornerShape(100.dp)
            ).padding(horizontal = 10.dp, vertical = 3.5.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                style = Body3,
                text = dDay,
                color = Gray600
            )
        }
        Spacer(
            modifier = Modifier.height(10.dp)
        )
        Text(
            style = Header1,
            text = title.ifEmpty { "등록된 일정이 없어요" },
            color = if (title.isEmpty()) Gray400 else Gray900
        )
        Spacer(
            modifier = Modifier.height(12.dp)
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier.size(20.dp),
                    painter = painterResource(id = R.drawable.ic_calender),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    colorFilter = ColorFilter.tint(color = Gray300)
                )

                Text(
                    style = Body3,
                    text = calendar,
                    color = Gray700
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier.size(20.dp),
                    painter = painterResource(id = R.drawable.ic_clock),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    colorFilter = ColorFilter.tint(color = Gray300)
                )
                Text(
                    text = timeLine,
                    style = Body3,
                    color = Gray700

                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier.size(20.dp),
                    painter = painterResource(id = R.drawable.ic_location),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    colorFilter = ColorFilter.tint(color = Gray300)
                )

                Text(
                    style = Body3,
                    text = location,
                    color = Gray700
                )
            }
        }
    }
}
