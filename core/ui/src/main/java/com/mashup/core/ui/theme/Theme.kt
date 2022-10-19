package com.mashup.core.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import com.mashup.core.ui.colors.Brand100
import com.mashup.core.ui.colors.Brand500
import com.mashup.core.ui.colors.Brand600

private val LightColorPalette = lightColors(
    primary = Brand500,
    primaryVariant = Brand600,
    secondary = Brand100
)

@Composable
fun MashUpTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colors = LightColorPalette,
        typography = MaterialTheme.typography,
        shapes = MaterialTheme.shapes,
        content = content
    )
}
