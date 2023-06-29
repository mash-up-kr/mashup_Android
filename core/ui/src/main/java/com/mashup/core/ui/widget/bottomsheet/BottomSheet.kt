package com.mashup.core.ui.widget.bottomsheet

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mashup.core.ui.colors.Gray900
import com.mashup.core.ui.typography.SubTitle1

@Composable
fun BottomSheetHandler(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Divider(
            modifier = Modifier.width(28.dp),
            color = Color(0xFFD9D9D9),
            thickness = 3.dp
        )
    }
}

@Composable
fun BottomSheetTitle(
    title: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 20.dp),
    ) {
        Text(
            text = title,
            color = Gray900,
            style = SubTitle1
        )
    }
}