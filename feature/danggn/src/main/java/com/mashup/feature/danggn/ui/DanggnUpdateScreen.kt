package com.mashup.feature.danggn.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mashup.core.ui.colors.Brand500
import com.mashup.core.ui.colors.Gray700
import com.mashup.core.ui.colors.Gray900
import com.mashup.core.ui.theme.MashUpTheme
import com.mashup.core.ui.typography.Body4
import com.mashup.core.ui.typography.Header2
import com.mashup.core.ui.widget.ButtonStyle
import com.mashup.core.ui.widget.MashUpButton
import com.mashup.core.ui.widget.MashUpToolbar
import com.mashup.feature.danggn.R

@Composable
fun DanggnUpdateScreen(
    modifier: Modifier = Modifier,
    onClickCloseButton: () -> Unit = {},
    onClickMoveDanggn: () -> Unit = {}
) {
    Column(
        modifier = modifier
    ) {
        DanggnUpdateContent(
            modifier = Modifier.fillMaxSize(),
            onClickCloseButton = onClickCloseButton,
            onClickMoveDanggn = onClickMoveDanggn
        )
    }
}

@Composable
fun DanggnUpdateContent(
    modifier: Modifier = Modifier,
    onClickCloseButton: () -> Unit = {},
    onClickMoveDanggn: () -> Unit = {}
) {
    Column(
        modifier = modifier
    ) {
        MashUpToolbar(
            modifier = Modifier.fillMaxWidth(),
            title = "당근 흔들기 랭킹 업데이트",
            showActionButton = true,
            onClickActionButton = onClickCloseButton
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                modifier = Modifier
                    .padding(top = 20.dp)
                    .padding(horizontal = 20.dp),
                text = buildAnnotatedString {
                    append("이제 당근 흔들기 랭킹은\n")
                    withStyle(
                        SpanStyle(
                            color = Brand500
                        )
                    ) {
                        append("2주마다 초기화")
                    }
                    append("돼요")
                },
                color = Gray900,
                style = Header2
            )

            Text(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .padding(horizontal = 20.dp),
                text = stringResource(R.string.content_period_danggn),
                color = Gray700,
                style = Body4
            )

            Text(
                modifier = Modifier
                    .padding(top = 67.dp)
                    .padding(horizontal = 20.dp),
                text = buildAnnotatedString {
                    append("2주의 랭킹 1위는\n")
                    withStyle(
                        SpanStyle(
                            color = Brand500
                        )
                    ) {
                        append("모두에게 한 마디를 공지")
                    }
                    append("할 수 있는\n리워드를 받을 수 있어요")
                },
                color = Gray900,
                style = Header2
            )


            Text(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .padding(horizontal = 20.dp),
                text = stringResource(R.string.content_reward_danggn),
                color = Gray700,
                style = Body4
            )
            Spacer(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            )
            MashUpButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                buttonStyle = ButtonStyle.INVERSE,
                text = "확인 완료",
                onClick = onClickCloseButton
            )
            MashUpButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 9.dp),
                text = "업데이트된 당근 흔들기 구경하기",
                onClick = onClickMoveDanggn
            )
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Preview(name = "화면이 작은 기기에서 스크롤 되는지 테스트", heightDp = 400)
@Preview
@Composable
private fun DanggnUpdateScreenPrev() {
    MashUpTheme {
        DanggnUpdateContent(modifier = Modifier.fillMaxWidth())
    }
}