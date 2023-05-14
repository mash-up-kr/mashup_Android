package com.mashup.core.ui.widget

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mashup.core.common.R
import com.mashup.core.common.model.Validation
import com.mashup.core.ui.colors.Brand500
import com.mashup.core.ui.colors.Gray400
import com.mashup.core.ui.colors.Gray600
import com.mashup.core.ui.colors.Green500
import com.mashup.core.ui.colors.Red500
import com.mashup.core.ui.theme.MashUpTheme
import com.mashup.core.ui.typography.Caption3
import com.mashup.core.ui.typography.Title2
import kotlinx.coroutines.delay

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MashUpTextField(
    modifier: Modifier = Modifier,
    text: String,
    onTextChanged: (String) -> Unit,
    labelText: String,
    requestFocus: Boolean,
    validation: Validation
) {
    var focus by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val cornerShape = RoundedCornerShape(12.dp)
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = modifier
    ) {
        BasicTextField(modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .height(84.dp)
            .animateContentSize()
            .clip(cornerShape)
            .run {
                if (validation == Validation.EMPTY) {
                    this
                } else {
                    border(
                        shape = cornerShape,
                        width = 1.dp,
                        color = when (validation) {
                            Validation.SUCCESS -> Brand500
                            else -> Red500
                        }
                    )
                }
            }
            .background(Color.White)
            .onFocusChanged {
                focus = it.hasFocus
            }
            .focusRequester(focusRequester),
            value = text,
            textStyle = Title2,
            singleLine = true,
            onValueChange = { onTextChanged(it) },
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .padding(start = 20.dp, end = 20.dp)
                        .fillMaxWidth()
                ) {
                    val textFieldState = text.isEmpty() && focus || text.isNotEmpty()
                    val paddingState by animateDpAsState(targetValue = if (textFieldState) 18.dp else 30.dp)
                    val textSizeState by animateFloatAsState(targetValue = if (textFieldState) 13f else 20f)
                    // 탈퇴할게요 힌트, 라벨
                    Text(
                        modifier = Modifier.padding(top = paddingState),
                        text = labelText,
                        fontSize = textSizeState.sp,
                        color = if (textFieldState) Gray600 else Gray400
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 36.dp, end = 14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(modifier = Modifier.weight(1f)) {
                            innerTextField()
                        }
                        val validationPainter: Int? = when (validation) {
                            Validation.FAILED -> R.drawable.ic_question_mark
                            Validation.SUCCESS -> R.drawable.ic_check
                            else -> null
                        }
                        validationPainter?.let {
                            Image(
                                modifier = Modifier
                                    .padding(start = 12.dp)
                                    .align(alignment = Alignment.CenterVertically),
                                painter = painterResource(id = validationPainter),
                                contentDescription = null,
                                colorFilter = if (validation == Validation.FAILED) {
                                    ColorFilter.tint(color = Red500)
                                } else {
                                    ColorFilter.tint(color = Green500)
                                }
                            )
                        }
                    }
                }
            })
        Text(
            modifier = Modifier.padding(top = 8.dp, start = 4.dp),
            text = setDescriptionText(validation),
            style = Caption3,
            color = if (validation == Validation.FAILED) Red500 else Gray600
        )
        LaunchedEffect(key1 = Unit) {
            if (requestFocus) {
                // delay를 줘야만 키보드가 올라옴 놀라운건 10L 보다 100L이 더 빨리올라옴;;
                focusRequester.requestFocus()
                delay(100L)
                keyboardController?.show()
            }
        }
    }
}

private fun setDescriptionText(codeState: Validation): String {
    return when (codeState) {
        Validation.SUCCESS -> {
            "위 문구를 입력해주세요."
        }
        Validation.FAILED -> {
            "문구가 동일하지 않아요"
        }
        Validation.EMPTY -> {
            "위 문구를 입력해주세요."
        }
    }
}

/**
 * 테스트를 위해서 focusBool을 넣어서 두개다 테스트
 */
@Preview("reverse - 기본 텍스트 필드")
@Composable
fun MashUpTextFieldPrev(
    labelText: String = "탈퇴할게요",
    validation: Validation = Validation.EMPTY,
    previousText: String = "",
    requestFocus: Boolean = false
) {
    var text by remember { mutableStateOf(previousText) }
    MashUpTheme {
        MashUpTextField(
            modifier = Modifier,
            text = text,
            onTextChanged = { newText -> text = newText },
            labelText = "탈퇴할게요",
            requestFocus = requestFocus,
            validation = validation
        )
    }
}
