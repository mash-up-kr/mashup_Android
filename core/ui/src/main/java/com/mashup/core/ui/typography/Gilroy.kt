package com.mashup.core.ui.typography

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.mashup.core.common.R
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp

private val gilroyFontFamily = FontFamily(
    Font(R.font.gilroy_light),
    Font(R.font.gilroy_extrabold, FontWeight.ExtraBold),
)

val GilroyNormal = TextStyle(
    fontFamily = gilroyFontFamily,
    fontSize = 16.sp,
    letterSpacing = (-0.01).em
)

val GilroyExtraBold = TextStyle(
    fontFamily = gilroyFontFamily,
    fontWeight = FontWeight.ExtraBold,
    fontSize = 16.sp,
    letterSpacing = (-0.01).em
)