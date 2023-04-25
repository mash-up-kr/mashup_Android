package com.mashup.feature.danggn.ranking

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import com.mashup.core.ui.colors.Black
import com.mashup.core.ui.colors.Gray400
import com.mashup.core.ui.colors.White
import com.mashup.core.ui.theme.MashUpTheme
import com.mashup.core.ui.typography.Body3
import com.mashup.core.ui.typography.SubTitle1
import com.mashup.core.ui.typography.Title1
import com.mashup.feature.danggn.R
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
            modifier = Modifier.fillMaxSize(),
            count = pages.size,
            state = pagerState,
            verticalAlignment = Alignment.Top,
        ) { _ ->
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(top = 12.dp)
            ) {
                itemsIndexed(items = list
                    .sortedByDescending {
                        it.totalShakeScore
                    }, key = { _, item ->
                    item.memberId
                }) { index, item ->
                    RankingContent(
                        modifier = Modifier.fillMaxWidth(),
                        index = index,
                        name = item.memberName,
                        shakeCount = item.totalShakeScore,
                    )
                }
            }
        }
    }
}

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
        modifier = modifier.padding(start = 20.dp, bottom = 25.dp, end = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row {
            Image(
                painter = painterResource(id = imageResourceList[index]),
                contentDescription = null
            )
            Text(
                // TODO 색깔 그라데이션
                modifier = Modifier.padding(start = 12.dp),
                text = name,
                style = SubTitle1,
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
        DanggnRankingContent(list = listOf())
    }
}
