package com.mashup.feature.danggn

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mashup.core.ui.colors.Brand500
import com.mashup.core.ui.colors.Gray100
import com.mashup.core.ui.colors.Gray50
import com.mashup.core.ui.colors.Gray500
import com.mashup.core.ui.colors.Gray600
import com.mashup.core.ui.colors.Gray700
import com.mashup.core.ui.colors.Gray900
import com.mashup.core.ui.theme.MashUpTheme
import com.mashup.core.ui.typography.Body1
import com.mashup.core.ui.typography.Body4
import com.mashup.core.ui.typography.Body5
import com.mashup.core.ui.typography.Caption2
import com.mashup.core.ui.typography.Caption3
import com.mashup.core.ui.typography.Header2
import com.mashup.core.ui.typography.SubTitle3
import com.mashup.core.ui.widget.MashUpToolbar
import com.mashup.core.common.R as CR

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DanggnInfoScreen(
    modifier: Modifier = Modifier,
    onClickBackButton: () -> Unit = {},
) {
    val scrollState = rememberScrollState()

    Column(modifier = modifier.background(Gray50)) {
        MashUpToolbar(
            title = "당근 흔들기 랭킹 정보",
            modifier = Modifier.fillMaxWidth(),
            showActionButton = true,
            onClickActionButton = onClickBackButton
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
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
                        .padding(top = 12.dp)
                        .padding(horizontal = 20.dp),
                    text = stringResource(R.string.content_period_danggn),
                    color = Gray700,
                    style = Body5
                )

                Spacer(modifier = Modifier.height(40.dp))

                Text(
                    modifier = Modifier
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

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    modifier = Modifier
                        .padding(horizontal = 20.dp),
                    text = stringResource(R.string.content_reward_danggn),
                    color = Gray700,
                    style = Body5
                )

                Spacer(modifier = Modifier.height(40.dp))
            }
            CompositionLocalProvider(
                LocalOverscrollConfiguration provides null
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    // Danggn Story
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painter = painterResource(id = CR.drawable.img_carrot_button),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(24.dp)
                                    .padding(end = 6.dp)
                            )

                            Text(text = "당근 흔들기 스토리", style = Header2, color = Gray900)
                        }

                        Text(
                            text = "\"힘들면 당근을 흔들어주세요\"",
                            modifier = Modifier.padding(top = 20.dp),
                            style = Body1,
                            color = Gray500
                        )

                        Text(
                            text = "매시업에서 프로젝트랑 스터디를 하면서 너무 힘들어하시는 분들.. 하지만 또 고통을 즐기는 매시업 크루분들 위해 작은 재미를 선사합니다. 작업이 빡센 기간에 “5252 힘들면 당근 흔들어~~!!\" 라는 밈을 참고해 그렇다면 정말 당근을 흔들 수 있게 해보자 하여 출시했습니다.",
                            modifier = Modifier.padding(top = 8.dp),
                            style = Body4,
                            color = Gray700
                        )

                        Text(
                            text = "성장통을 즐기는 매시업 크루분들!!\n" + "이제 당근을 흔들고 매시업 랭킹에 이름을 올려보세요!\n" + "나 좀 힘들다..?",
                            modifier = Modifier.padding(top = 20.dp),
                            style = Body4,
                            color = Gray700
                        )

                        Row(
                            modifier = Modifier.padding(top = 40.dp, bottom = 20.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = CR.drawable.ic_mashup),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(24.dp)
                                    .padding(end = 6.dp)
                            )

                            Text(
                                text = "From. 13기 브랜딩팀 출첵앱TF",
                                style = SubTitle3,
                                color = Gray900
                            )
                        }
                    }

                    // Footer
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = Gray100)
                            .padding(start = 20.dp, top = 20.dp, end = 20.dp, bottom = 100.dp)
                    ) {
                        Text(
                            text = "13기 브랜딩팀 출첵앱TF 빛나는 크루원",
                            style = Caption2,
                            color = Gray600
                        )

                        Text(
                            text = "Product Design : 배선영",
                            modifier = Modifier.padding(top = 12.dp),
                            style = Caption3,
                            color = Gray500
                        )

                        Text(
                            text = "Backend : 정종민 호선우",
                            modifier = Modifier.padding(top = 8.dp),
                            style = Caption3,
                            color = Gray500
                        )

                        Text(
                            text = "iOS : 김남수 이재용",
                            modifier = Modifier.padding(top = 8.dp),
                            style = Caption3,
                            color = Gray500
                        )

                        Text(
                            text = "Android : 양민욱 안석주 정민지",
                            modifier = Modifier.padding(top = 8.dp),
                            style = Caption3,
                            color = Gray500
                        )

                        Text(
                            text = "이들의 한마디",
                            modifier = Modifier.padding(top = 20.dp),
                            style = Caption2,
                            color = Gray600
                        )

                        Text(
                            text = "잘 써줬으면 좋겠ㄷr...",
                            modifier = Modifier.padding(top = 12.dp),
                            style = Caption3,
                            color = Gray500
                        )
                    }
                }
            }
        }

    }
}

@Preview
@Composable
fun PreviewInfo() {
    MashUpTheme {
        DanggnInfoScreen()
    }
}
