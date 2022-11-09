package com.mashup.core.ui.typography

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.mashup.core.common.R
import com.mashup.core.ui.colors.White

private val mashupFontFamily = FontFamily(
    Font(R.font.pretendard_regular),
    Font(R.font.pretendard_medium, FontWeight.Medium),
    Font(R.font.pretendard_semibold, FontWeight.SemiBold),
    Font(R.font.pretendard_bold, FontWeight.Bold)
)

val Header1 = TextStyle(
    fontFamily = mashupFontFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 24.sp,
    letterSpacing = (-1).sp
)

val Header2 = TextStyle(
    fontFamily = mashupFontFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 20.sp,
    letterSpacing = (-1).sp
)

val Title1 = TextStyle(
    fontFamily = mashupFontFamily,
    fontWeight = FontWeight.SemiBold,
    fontSize = 20.sp,
    letterSpacing = (-1).sp
)

val Title2 = TextStyle(
    fontFamily = mashupFontFamily,
    fontWeight = FontWeight.Medium,
    fontSize = 20.sp,
    letterSpacing = (-1).sp
)

val Title3 = TextStyle(
    fontFamily = mashupFontFamily,
    fontWeight = FontWeight.SemiBold,
    fontSize = 18.sp,
    letterSpacing = (-1).sp
)

val SubTitle1 = TextStyle(
    fontFamily = mashupFontFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 16.sp,
    letterSpacing = (-1).sp
)

val SubTitle2 = TextStyle(
    fontFamily = mashupFontFamily,
    fontWeight = FontWeight.SemiBold,
    fontSize = 16.sp,
    letterSpacing = (-1).sp
)

val Body1 = TextStyle(
    fontFamily = mashupFontFamily,
    fontWeight = FontWeight.Medium,
    fontSize = 16.sp,
    letterSpacing = (-1).sp
)

val Body2 = TextStyle(
    fontFamily = mashupFontFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 16.sp,
    letterSpacing = (-1).sp
)

val Body3 = TextStyle(
    fontFamily = mashupFontFamily,
    fontWeight = FontWeight.Medium,
    fontSize = 14.sp,
    letterSpacing = (-1).sp
)

val Body4 = TextStyle(
    fontFamily = mashupFontFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 14.sp,
    letterSpacing = (-1).sp
)

val Caption1 = TextStyle(
    fontFamily = mashupFontFamily,
    fontWeight = FontWeight.Medium,
    fontSize = 13.sp,
    letterSpacing = (-1).sp
)

val Caption2 = TextStyle(
    fontFamily = mashupFontFamily,
    fontWeight = FontWeight.Medium,
    fontSize = 12.sp,
    letterSpacing = (-1).sp
)

val Caption3 = TextStyle(
    fontFamily = mashupFontFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 12.sp,
    letterSpacing = (-1).sp
)

@Composable
fun MashTextView(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = mashupFontFamily,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    style: TextStyle = LocalTextStyle.current
) {
    Text(
        text,
        modifier,
        color,
        fontSize,
        fontStyle,
        fontWeight,
        fontFamily,
        letterSpacing,
        textDecoration,
        textAlign,
        lineHeight, overflow, softWrap, maxLines, onTextLayout, style
    )
}

@Preview
@Composable
fun PrevMashupTextView() {
    MaterialTheme {
        Column(modifier = Modifier.background(color = White)) {
            MashTextView(text = "Regular 테스트")
            MashTextView(text = "Medium 테스트", fontWeight = FontWeight.Medium)
            MashTextView(text = "SemiBold 테스트", fontWeight = FontWeight.SemiBold)
            MashTextView(text = "Bold 테스트", fontWeight = FontWeight.Bold)
        }
    }
}
