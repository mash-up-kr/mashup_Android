package com.mashup.compose.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import com.mashup.compose.colors.Primary100
import com.mashup.compose.colors.Primary500
import com.mashup.compose.colors.Primary600

private val LightColorPalette = lightColors(
    primary = Primary500,
    primaryVariant = Primary600,
    secondary = Primary100
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