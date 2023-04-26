package com.mashup.feature.setting.menu

import androidx.annotation.ColorRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Divider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mashup.core.ui.colors.Gray100
import com.mashup.core.ui.theme.MashUpTheme
import com.mashup.core.ui.typography.Body4
import com.mashup.core.ui.typography.SubTitle2
import com.mashup.core.ui.widget.MashUpSwitch
import com.mashup.core.common.R as CR

@Composable
fun BasicSettingItem(
    text: String,
    @ColorRes textColorRes: Int,
    onClickItem: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clickable(onClick = onClickItem)
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 18.dp),
            text = text,
            style = SubTitle2,
            color = colorResource(id = textColorRes)
        )
        Divider(
            modifier = modifier
                .fillMaxWidth()
                .height(1.dp)
                .align(Alignment.BottomCenter),
            color = Gray100
        )
    }
}

@Composable
fun RightArrowSettingItem(
    text: String,
    @ColorRes textColorRes: Int,
    onClickItem: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clickable(onClick = onClickItem)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier
                    .padding(vertical = 18.dp)
                    .padding(start = 20.dp)
                    .weight(1f),
                text = text,
                style = SubTitle2,
                color = colorResource(id = textColorRes)
            )
            Image(
                modifier = Modifier.padding(end = 20.dp),
                painter = painterResource(id = CR.drawable.ic_chevron_right),
                contentDescription = "$text setting right arrow"
            )
        }
        Divider(
            modifier = modifier
                .fillMaxWidth()
                .height(1.dp)
                .align(Alignment.BottomCenter),
            color = Gray100
        )
    }
}

@Composable
fun FcmToggleSettingItem(
    title: String,
    @ColorRes titleColorRes: Int,
    description: String,
    @ColorRes descriptionRes: Int,
    onCheckedChange: (Boolean) -> Unit,
    checked: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .toggleable(
                value = checked,
                onValueChange = onCheckedChange,
                role = Role.Switch
            )
    ) {
        Column(
            modifier = Modifier.padding(vertical = 18.dp)
        ) {
            Row(
                modifier = Modifier.padding(end = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier
                        .padding(start = 20.dp)
                        .padding(bottom = 6.5.dp)
                        .weight(1f),
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
                    .padding(start = 20.dp),
                text = description,
                color = colorResource(id = descriptionRes),
                style = Body4
            )
        }
        Divider(
            modifier = modifier
                .fillMaxWidth()
                .height(1.dp)
                .align(Alignment.BottomCenter),
            color = Gray100
        )
    }
}

@Preview
@Composable
fun BasicSettingItemPrev() {
    MashUpTheme {
        Surface(color = MaterialTheme.colors.onBackground) {
            BasicSettingItem(
                text = "설정",
                textColorRes = CR.color.black,
                onClickItem = { }
            )
        }
    }
}

@Preview
@Composable
fun ArrowSettingItemPrev() {
    MashUpTheme {
        Surface(color = MaterialTheme.colors.onBackground) {
            RightArrowSettingItem(
                text = "설정",
                textColorRes = CR.color.black,
                onClickItem = { }
            )
        }
    }
}

@Preview
@Composable
fun FcmToggleSettingItemPrev() {
    MashUpTheme {
        Surface(color = MaterialTheme.colors.onBackground) {
            FcmToggleSettingItem(
                title = "매시업 소식 알림",
                titleColorRes = CR.color.black,
                description = "출석 시간과 세미나 일정, 그리고 활동점수 \n 업데이트 소식을 받을 수 있어요",
                descriptionRes = CR.color.gray500,
                onCheckedChange = { },
                checked = true
            )
        }
    }
}
