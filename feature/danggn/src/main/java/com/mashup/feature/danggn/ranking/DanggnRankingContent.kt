package com.mashup.feature.danggn.ranking

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import com.mashup.core.ui.colors.Black
import com.mashup.core.ui.colors.Gray200
import com.mashup.core.ui.colors.Gray400
import com.mashup.core.ui.colors.Gray500
import com.mashup.core.ui.colors.Gray900
import com.mashup.core.ui.colors.White
import com.mashup.core.ui.colors.rankingOneGradient
import com.mashup.core.ui.colors.rankingThreeGradient
import com.mashup.core.ui.colors.rankingTwoGradient
import com.mashup.core.ui.theme.MashUpTheme
import com.mashup.core.ui.typography.Body3
import com.mashup.core.ui.typography.GilroyExtraBold
import com.mashup.core.ui.typography.SubTitle1
import com.mashup.core.ui.typography.Title1
import com.mashup.core.ui.widget.MashUpButton
import com.mashup.feature.danggn.R
import com.mashup.feature.danggn.data.dto.DanggnMemberRankResponse
import kotlinx.coroutines.launch
import com.mashup.feature.danggn.data.dto.DanggnMemberRankResponse as DtoRankResponse

@OptIn(ExperimentalPagerApi::class)
@Composable
fun DanggnRankingContent(
    modifier: Modifier = Modifier,
    list: List<DtoRankResponse>
) {
    val pages = listOf("크루원", "플랫폼 팀")
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = modifier.background(White)) {
        Text(
            modifier = Modifier
                .padding(start = 20.dp, top = 24.dp)
                .fillMaxWidth(),
            text = "랭킹",
            style = Title1,
        )
        TabRow(
            modifier = Modifier,
            selectedTabIndex = pagerState.currentPage,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    modifier = Modifier
                        .pagerTabIndicatorOffset(
                            pagerState = pagerState,
                            tabPositions = tabPositions
                        )
                        .padding(horizontal = 60.dp),
                    color = Black
                )
            }
        ) {
            pages.forEachIndexed { index, title ->
                Tab(
                    modifier = Modifier
                        .background(White),
                    text = {
                        MashUpPagerColorAnimator(title, pagerState.currentPage == index)
                    },
                    selected = pagerState.currentPage == index,
                    onClick = {
                        coroutineScope.launch {
                            pagerState.scrollToPage(index)
                        }
                    })
            }
        }
        // TODO 내 랭킹 추가하기
        HorizontalPager(
            modifier = Modifier
                .fillMaxSize(),
            count = pages.size,
            state = pagerState,
            verticalAlignment = Alignment.Top,
        ) { _ ->
            PagerContents(list)
        }
    }
}

@Composable
private fun PagerContents(list: List<DanggnMemberRankResponse>) {
    val listState = rememberLazyListState()
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = listState,
        contentPadding = PaddingValues(top = 12.dp)
    ) {
        itemsIndexed(
            items = list.sortedByDescending {
                it.totalShakeScore
            },
            key = { _, item ->
                item.memberId
            }) { index, item ->
            RankingContent(
                modifier = Modifier.fillMaxWidth(),
                index = index,
                name = item.memberName,
                shakeCount = item.totalShakeScore,
            )
            if (index == 2) {
                DrawDottedLine()
            }
        }
        // TODO index 11일때 가리는 것 추가해야됨 지금은 넣으면 안보이기 때문에 안넣음
        item {
            Text(
                modifier = Modifier
                    .padding(top = 28.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                text = "당근을 더 흔들어서 랭킹 안에 들어보세요",
                style = Body3,
                color = Gray500
            )
        }

        item {
            val coroutineScope = rememberCoroutineScope()
            MashUpButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 20.dp,
                        end = 20.dp,
                        top = 28.dp,
                        bottom = 20.dp
                    ),
                text = "당근 더 흔들기",
                onClick = {
                    coroutineScope.launch {
                        listState.animateScrollToItem(index = 0)
                    }
                })
        }
    }
}

@Composable
private fun DrawDottedLine() {
    val pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f))
    Canvas(
        Modifier
            .fillMaxSize()
            .padding(
                start = 20.dp,
                end = 20.dp,
                top = 6.dp,
                bottom = 6.dp
            )
            .height(1.dp)
    ) {
        drawLine(
            color = Gray200,
            start = Offset(0f, 0f),
            end = Offset(size.width, 0f),
            pathEffect = pathEffect
        )
    }
}

@OptIn(ExperimentalTextApi::class)
@Composable
private fun RankingContent(
    modifier: Modifier,
    index: Int,
    name: String,
    shakeCount: Int,
) {
    val imageResourceList =
        listOf(R.drawable.img_rank_1, R.drawable.img_rank_2, R.drawable.img_rank_3)
    Row(
        modifier = modifier.padding(horizontal = 20.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row {
            if (index in imageResourceList.indices) {
                Image(
                    modifier = Modifier
                        .size(20.dp)
                        .align(Alignment.CenterVertically),
                    painter = painterResource(id = imageResourceList[index]),
                    contentDescription = null
                )
            } else { //3 ~ 10
                Text(
                    modifier = Modifier
                        .size(20.dp)
                        .align(Alignment.CenterVertically),
                    text = "${(index + 1)}",
                    textAlign = TextAlign.Center,
                    style = GilroyExtraBold,
                    color = Gray400
                )
            }
            Text(
                modifier = Modifier
                    .padding(start = 12.dp),
                text = name,
                style = SubTitle1.copy(
                    brush = Brush.linearGradient(
                        when (index) { // 반드시 2개 이상의 컬러가 필요해서 Gray900 넣어줬습니다
                            0 -> rankingOneGradient
                            1 -> rankingTwoGradient
                            2 -> rankingThreeGradient
                            else -> listOf(Gray900, Gray900)
                        }
                    )
                ),
                textAlign = TextAlign.Center
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                modifier = Modifier.size(10.dp),
                painter = painterResource(id = com.mashup.core.common.R.drawable.img_carrot_button),
                contentDescription = null
            )
            Text(
                modifier = Modifier.padding(start = 4.dp),
                text = shakeCount.toString(), // 컴마 표시 유틸 추가하기
                style = Body3
            )
        }
    }
}

@Composable
private fun MashUpPagerColorAnimator(
    title: String, selected: Boolean
) {
    val textColorAnimation by animateColorAsState(
        targetValue = if (selected) Black else Gray400
    )
    Text(
        text = title,
        textAlign = TextAlign.Start,
        color = textColorAnimation
    )
}

@Preview("Ranking Preview")
@Composable
fun MashUpRankingPreview() {
    MashUpTheme {
        DanggnRankingContent(
            list = listOf(
                DanggnMemberRankResponse(
                    39, "정종노드", 150
                ),
                DanggnMemberRankResponse(
                    40, "정종드투", 151
                ),
                DanggnMemberRankResponse(
                    41, "정종민", 152
                ),
                DanggnMemberRankResponse(
                    42, "정종웹", 153
                ),
                DanggnMemberRankResponse(
                    43, "정종오스", 154
                ),
                DanggnMemberRankResponse(
                    44, "정종자인", 155
                ),
                DanggnMemberRankResponse(
                    45, "정종노드", 150
                ),
                DanggnMemberRankResponse(
                    46, "정종드투", 151
                ),
                DanggnMemberRankResponse(
                    47, "정종민", 152
                ),
                DanggnMemberRankResponse(
                    48, "정종웹", 153
                ),
                DanggnMemberRankResponse(
                    49, "정종오스", 154
                ),
                DanggnMemberRankResponse(
                    50, "정종자인", 155
                ),
                DanggnMemberRankResponse(
                    51, "정종노드", 150
                ),
                DanggnMemberRankResponse(
                    52, "정종드투", 151
                ),
                DanggnMemberRankResponse(
                    53, "정종민", 152
                ),
                DanggnMemberRankResponse(
                    54, "정종웹", 153
                ),
                DanggnMemberRankResponse(
                    55, "정종오스", 154
                ),
                DanggnMemberRankResponse(
                    56, "정종자인", 155
                ),
            )
        )
    }
}
