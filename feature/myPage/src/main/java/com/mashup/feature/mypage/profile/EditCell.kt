package com.mashup.feature.mypage.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mashup.core.ui.colors.Brand500
import com.mashup.core.ui.colors.Gray100
import com.mashup.core.ui.colors.Gray300
import com.mashup.core.ui.colors.Gray800
import com.mashup.core.ui.typography.Body3
import com.mashup.core.ui.typography.Body4

@Composable
fun MyPageEditReadOnlyCell(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier.width(80.dp),
            text = title,
            style = Body3,
            color = Gray800
        )

        Text(
            modifier = Modifier.padding(start = 12.dp),
            text = value,
            style = Body4,
            color = Brand500
        )
    }
}

@Composable
fun MyPageEditWriteCell(
    title: String,
    value: String,
    hint: String,
    onValueChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier.width(80.dp),
            text = title,
            style = Body3,
            color = Gray800
        )

        BasicTextField(
            modifier = Modifier.padding(start = 12.dp),
            value = value,
            onValueChange = onValueChanged,
            textStyle = Body4,
            decorationBox = { innerTextField ->
                Box {
                    if (value.isEmpty()) {
                        Text(
                            text = hint,
                            style = Body4,
                            color = Gray300
                        )
                    }
                    innerTextField()
                }
            }
        )
    }
}

@Composable
fun MyPageEditCellDivider(modifier: Modifier = Modifier) {
    Divider(
        modifier = Modifier.fillMaxWidth(),
        color = Gray100,
        thickness = 1.dp
    )
}