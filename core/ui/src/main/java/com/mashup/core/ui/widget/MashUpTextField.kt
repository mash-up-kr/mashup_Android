package com.mashup.core.ui.widget

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.OverscrollEffect
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.overscroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
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
import com.mashup.core.ui.typography.Caption1
import com.mashup.core.ui.typography.Caption3
import com.mashup.core.ui.typography.Title2
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Composable
fun MashUpTextField(
    modifier: Modifier = Modifier,
    text: String,
    onTextChanged: (String) -> Unit,
    placeHolderText: String,
    label: String,
    focusBool: Boolean,
    codeState: Validation
) {
    val focusRequester = remember { FocusRequester() }
    val rememberCoroutineScope = rememberCoroutineScope()
    var focus by remember {
        mutableStateOf(focusBool)
    }
    val scrollState = rememberScrollState(0)
    Column(
        modifier = Modifier
    ) {
        BasicTextField(modifier = modifier
            .fillMaxWidth()
            .padding(4.dp)
            .height(84.dp)
            .focusRequester(focusRequester)
            .clip(RoundedCornerShape(12.dp))
            .border(
                shape = RoundedCornerShape(12.dp),
                width = 1.dp,
                color = when (codeState) {
                    Validation.EMPTY -> Color.White
                    Validation.SUCCESS -> Brand500
                    else -> Red500
                }
            )
            .background(Color.White)
            .onFocusChanged {
                rememberCoroutineScope.launch {
                    Log.d("tjrwn", "MashUpTextField: ${text.length}")
                    scrollState.scrollTo(text.length)
                }
                focus = it.hasFocus
            },
            value = text,
            textStyle = Title2,
            singleLine = true,
            onValueChange = onTextChanged,
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .padding(start = 20.dp, end = 20.dp)
                        .fillMaxWidth()
                ) {
                    if (focus || text.isNotEmpty()) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(
                                modifier = Modifier.padding(top = 18.dp),
                                text = label,
                                style = Caption1,
                                color = Gray600
                            )
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(end = 14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(modifier = Modifier.weight(1f)) {
                                    innerTextField()
                                }
                                if (text.isEmpty().not()) {
                                    Image(
                                        modifier = Modifier.padding(start = 12.dp),
                                        painter = if (codeState == Validation.FAILED) painterResource(id = R.drawable.ic_question_mark)
                                        else painterResource(id = R.drawable.ic_check),
                                        contentDescription = null,
                                        colorFilter = if (codeState == Validation.FAILED) ColorFilter.tint(
                                            color = Red500
                                        ) else ColorFilter.tint(color = Green500)
                                    )
                                }
                            }
                        }
                    } else {
                        if (text.isEmpty()) {
                            Text(
                                modifier = Modifier.align(Alignment.CenterStart),
                                text = placeHolderText,
                                style = Title2,
                                color = Gray400,
                                lineHeight = 24.sp
                            )
                        }
                    }
                }
            })
        Text(
            modifier = Modifier.padding(top = 8.dp, start = 4.dp),
            text = setDescriptionText(codeState),
            style = Caption3,
            color = if (codeState == Validation.FAILED) Red500 else Gray600
        )
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

@Preview("기본 텍스트 필드")
@Composable
fun MashUpTextFieldPrev() {
    var text by remember { mutableStateOf("") }
    val validation = Validation.FAILED
    MashUpTheme {
        MashUpTextField(
            modifier = Modifier,
            text = text,
            onTextChanged = { newText ->
                text = newText
            },
            label = "label",
            placeHolderText = "탈퇴할게요",
            focusBool = false,
            codeState = validation
        )
    }
}

/**
 * 테스트를 위해서 focusBool을 넣어서 두개다 테스트
 */
@Preview("reverse - 기본 텍스트 필드")
@Composable
fun MashUpTextFieldPrevReverse() {
    var text by remember { mutableStateOf("ABCDEFGHIJKLMNOPQRSTUVWXYZ01234567890") }
    val validation = Validation.SUCCESS
    MashUpTheme {
        MashUpTextField(
            modifier = Modifier,
            text = text,
            onTextChanged = { newText ->
                text = newText
            },
            label = "label",
            placeHolderText = "탈퇴할게요",
            focusBool = true,
            codeState = validation
        )
    }
}

// 매개변수
// 애니메이션
// modifier 알기
