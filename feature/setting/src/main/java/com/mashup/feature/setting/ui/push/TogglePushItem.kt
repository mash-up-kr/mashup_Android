package com.mashup.feature.setting.ui.push

import androidx.annotation.ColorRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mashup.core.common.R
import com.mashup.core.ui.colors.Gray100
import com.mashup.core.ui.theme.MashUpTheme
import com.mashup.core.ui.typography.Body4
import com.mashup.core.ui.typography.SubTitle2
import com.mashup.core.ui.widget.MashUpSwitch

@Composable
fun TogglePushItem(
    title: String,
    @ColorRes titleColorRes: Int,
    description: String,
    @ColorRes descriptionRes: Int,
    checked: Boolean,
    modifier: Modifier = Modifier,
    onCheckedChange: (Boolean) -> Unit = {},
) {
    Column(
        modifier = modifier
            .toggleable(
                value = checked,
                onValueChange = onCheckedChange,
                role = Role.Switch
            )
    ) {
        Column(
            modifier = Modifier.padding(vertical = 18.dp, horizontal = 20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    color = colorResource(id = titleColorRes),
                    style = SubTitle2,
                    text = title
                )

                MashUpSwitch(
                    checked = checked,
                    onCheckedChange = onCheckedChange
                )
            }
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 60.dp),
                text = description,
                color = colorResource(id = descriptionRes),
                style = Body4
            )
        }
        Divider(
            modifier = modifier
                .fillMaxWidth()
                .height(1.dp),
            color = Gray100
        )
    }
}

@Preview
@Composable
fun TogglePushItemPrev() {
    MashUpTheme {
        Surface(color = MaterialTheme.colors.onBackground) {
            TogglePushItem(
                title = "매시업 소식 알림",
                titleColorRes = R.color.black,
                description = "출석 시간과 세미나 일정, 그리고 활동점수 \n 업데이트 소식을 받을 수 있어요",
                descriptionRes = R.color.gray500,
                onCheckedChange = { },
                checked = true
            )
        }
    }
}
