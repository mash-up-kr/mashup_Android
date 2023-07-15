package com.mashup.feature.danggn

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.mashup.core.ui.colors.*
import com.mashup.core.ui.typography.*
import com.mashup.core.ui.widget.MashUpToolbar
import com.mashup.core.common.R as CR

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DanggnInfoScreen(
    modifier: Modifier = Modifier,
    hasMoveToDanggnButton: Boolean = true,
    onClickBackButton: () -> Unit,
) {
    val scrollState = rememberScrollState()

    Column(modifier = modifier) {
        MashUpToolbar(
            title = "",
            modifier = Modifier.fillMaxWidth(),
            showBackButton = true,
            onClickBackButton = onClickBackButton
        )

        CompositionLocalProvider(
            LocalOverscrollConfiguration provides null
        ) {
            Column(modifier = Modifier.verticalScroll(scrollState)) {
                // Danggn Story
                Column(modifier = Modifier.padding(20.dp)) {
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
                        modifier = Modifier.padding(top = 40.dp),
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
                        .background(Gray100)
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
