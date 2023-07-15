package com.mashup.feature.danggn.ranking

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.google.accompanist.pager.rememberPagerState
import com.mashup.core.common.utils.thousandFormat
import com.mashup.core.ui.colors.*
import com.mashup.core.ui.extenstions.noRippleClickable
import com.mashup.core.ui.theme.MashUpTheme
import com.mashup.core.ui.typography.*
import com.mashup.core.ui.widget.MashUpButton
import com.mashup.feature.danggn.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun DanggnRankingContent(
    modifier: Modifier = Modifier,
    personalRankList: List<DanggnRankingViewModel.RankingItem>,
    myPersonalRank: DanggnRankingViewModel.RankingItem,
    platformRankList: List<DanggnRankingViewModel.RankingItem>,
    platformRank: DanggnRankingViewModel.RankingItem,
    onClickScrollTopButton: () -> Unit = {},
    onChangedTabIndex: (index: Int) -> Unit = {}
) {
    val pages = listOf("크루원", "플랫폼 팀")
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(pagerState.currentPage) {
        onChangedTabIndex(pagerState.currentPage)
    }

    Column(modifier = modifier.background(Gray50)) {
        TabRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            containerColor = Gray50,
            selectedTabIndex = pagerState.currentPage,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    modifier = Modifier
                        .tabIndicatorOffset(tabPositions[pagerState.currentPage])
                        .clip(RoundedCornerShape(20.dp)),
                    color = Black,
                    height = 2.dp
                )
            },
            divider = {}
        ) {
            pages.forEachIndexed { index, title ->
                MashUpPagerColorAnimatedTab(
                    modifier = Modifier
                        .fillMaxWidth()
                        .noRippleClickable {
                            coroutineScope.launch {
                                pagerState.scrollToPage(index)
                            }
                        },
                    title = title,
                    selected = pagerState.currentPage == index
                )
            }
        }

        HorizontalPager(
            modifier = Modifier
                .fillMaxSize(),
            count = pages.size,
            state = pagerState,
            verticalAlignment = Alignment.Top,
        ) { index ->
            /**
             * 아직 아무도 흔들지 않아요 테스트는, 아래의 리스트를 emptyList()로 주세요!
             */
            PagerContents(
                personalRankList,
                myPersonalRank,
                platformRankList,
                platformRank,
                index,
                onClickScrollTopButton = onClickScrollTopButton
            )
        }
    }
}

@Composable
private fun PagerContents(
    personalRankList: List<DanggnRankingViewModel.RankingItem>,
    myPersonalRank: DanggnRankingViewModel.RankingItem,
    platformRankList: List<DanggnRankingViewModel.RankingItem>,
    platformRank: DanggnRankingViewModel.RankingItem,
    pagerIndex: Int,
    onClickScrollTopButton: () -> Unit = {}
) {
    if (personalRankList.isEmpty()) {
        Text(
            modifier = Modifier,
            textAlign = TextAlign.Center,
            text = "아직 아무도 당근을 흔들지 않았어요\n바로 당근을 흔들어서 랭킹 안에 들어보세요!",
            color = Gray400,
            style = Caption1
        )
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            /**
             * 내 랭킹, 내 플랫폼 랭킹을 표시할 때, viewModel에서 indexOfFirst 함수를 사용했는데,
             * 매치되는 값이 없을 때 -1을 리턴합니다. -1의 경우 빈문자열로 치환했기 때문에
             * 해당 텍스트가 empty이면 + 페이지 인덱스를 보고 MyRanking을 그릴지 말지 분기합니다
             */
            if (myPersonalRank.text.isNotEmpty() && pagerIndex == 0
                || platformRank.text.isNotEmpty() && pagerIndex == 1
            ) {
                MyRanking(if (pagerIndex == 0) myPersonalRank else platformRank, pagerIndex)
            }

            (if (pagerIndex == 0) personalRankList else platformRankList).forEachIndexed { index, rankingUiState ->
                key(rankingUiState.memberId) {
                    /**
                     * 크루원 랭킹은 매시업 인원 전체, 플랫폼 랭킹은 6개 보여줍니다.
                     */
                    RankingContent(
                        modifier = Modifier.fillMaxWidth(),
                        index = index,
                        item = rankingUiState
                    )
                    if (index == 2) {
                        DrawDottedLine()
                    }
                }
            }

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
                onClick = onClickScrollTopButton
            )
        }
    }
}

@Composable
private fun MyRanking(
    personalRank: DanggnRankingViewModel.RankingItem,
    pagerIndex: Int,
) {
    val isAllCrewRanking = pagerIndex == 0
    val myRankingText = if (isAllCrewRanking) "내 랭킹 " else "내 팀 랭킹 "
    MyRankingInnerContent(myRankingText, personalRank)
}

@Composable
private fun MyRankingInnerContent(
    myRankingText: String,
    matchedPersonalRanking: DanggnRankingViewModel.RankingItem,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 4.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(color = Brand200),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row {
            Text(
                modifier = Modifier.padding(start = 16.dp),
                text = myRankingText,
                style = Body3
            )
            Text(
                modifier = Modifier,
                text = matchedPersonalRanking.text,
                style = Body3,
                color = Brand600
            )
        }

        Row(
            modifier = Modifier.padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier
                    .padding(end = 5.dp)
                    .size(10.dp),
                painter = painterResource(id = com.mashup.core.common.R.drawable.img_carrot_button),
                contentDescription = null
            )
            Text(
                modifier = Modifier.padding(end = 16.dp),
                text = thousandFormat(matchedPersonalRanking.totalShakeScore),
                color = Gray900,
                style = Caption1
            )
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
    item: DanggnRankingViewModel.RankingItem,
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
            } else { // 4등부터 나머지 다보여 줌
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
            val gradientGray300 = listOf(Gray300, Gray300)
            val gradientGray900 = listOf(Gray900, Gray900)
            Text(
                modifier = Modifier
                    .padding(start = 12.dp),
                text = when (item) {
                    is DanggnRankingViewModel.RankingItem.Ranking -> item.text
                    is DanggnRankingViewModel.RankingItem.EmptyRanking -> "아직 ${index + 1}위가 없어요"
                    is DanggnRankingViewModel.RankingItem.PlatformRanking -> item.text
                    is DanggnRankingViewModel.RankingItem.MyRanking -> ""
                    is DanggnRankingViewModel.RankingItem.MyPlatformRanking -> ""
                },
                style = SubTitle1.copy(
                    brush = Brush.linearGradient(
                        when (index) {
                            0 -> if (item !is DanggnRankingViewModel.RankingItem.EmptyRanking) rankingOneGradient else gradientGray300
                            1 -> if (item !is DanggnRankingViewModel.RankingItem.EmptyRanking) rankingTwoGradient else gradientGray300
                            2 -> if (item !is DanggnRankingViewModel.RankingItem.EmptyRanking) rankingThreeGradient else gradientGray300
                            else -> if (item !is DanggnRankingViewModel.RankingItem.EmptyRanking) gradientGray900 else gradientGray300
                        }
                    )
                ),
                textAlign = TextAlign.Center
            )
        }
        if (item.totalShakeScore > 0) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    modifier = Modifier.size(10.dp),
                    painter = painterResource(id = com.mashup.core.common.R.drawable.img_carrot_button),
                    contentDescription = null
                )
                Text(
                    modifier = Modifier.padding(start = 4.dp),
                    text = thousandFormat(item.totalShakeScore),
                    style = Body3
                )
            }
        }
    }
}

@Composable
private fun MashUpPagerColorAnimatedTab(
    title: String,
    selected: Boolean,
    modifier: Modifier = Modifier
) {
    val textColorAnimation by animateColorAsState(
        targetValue = if (selected) Black else Gray400
    )
    Text(
        modifier = modifier.padding(bottom = 8.dp),
        text = title,
        textAlign = TextAlign.Center,
        color = textColorAnimation,
        style = SubTitle1
    )
}

@Preview("Ranking Preview")
@Composable
fun MashUpRankingPreview() {
    MashUpTheme {
        DanggnRankingContent(
            personalRankList = listOf(
                DanggnRankingViewModel.RankingItem.Ranking(
                    "39", "정종노드", 150
                ),
                DanggnRankingViewModel.RankingItem.Ranking(
                    "56", "정종드투", 1510
                ),
                DanggnRankingViewModel.RankingItem.Ranking(
                    "57", "정종드썬더일레븐", 1511
                ),
                DanggnRankingViewModel.RankingItem.Ranking(
                    "58", "정종드썬더트웰브", 1512
                ),
                DanggnRankingViewModel.RankingItem.Ranking(
                    "59", "정종드썬더썰틴", 1513
                ),
                DanggnRankingViewModel.RankingItem.Ranking(
                    "60", "정종드썬더피프티피프티", 1514
                ),
                DanggnRankingViewModel.RankingItem.EmptyRanking(),
                DanggnRankingViewModel.RankingItem.EmptyRanking(),
                DanggnRankingViewModel.RankingItem.EmptyRanking(),
                DanggnRankingViewModel.RankingItem.EmptyRanking(),
                DanggnRankingViewModel.RankingItem.EmptyRanking(),
                DanggnRankingViewModel.RankingItem.EmptyRanking()
            ).sortedByDescending { it.totalShakeScore },
            myPersonalRank = DanggnRankingViewModel.RankingItem.MyRanking(
                memberId = "60", totalShakeScore = 1514, text = "1위",
            ),
            platformRankList = listOf(
                DanggnRankingViewModel.RankingItem.PlatformRanking(
                    memberId = "Android",
                    text = "Android", totalShakeScore = 120,
                ),
                DanggnRankingViewModel.RankingItem.PlatformRanking(
                    memberId = "iOS",
                    text = "iOS", totalShakeScore = 119,
                ),
                DanggnRankingViewModel.RankingItem.EmptyRanking(),
                DanggnRankingViewModel.RankingItem.EmptyRanking(),
                DanggnRankingViewModel.RankingItem.EmptyRanking(),
                DanggnRankingViewModel.RankingItem.EmptyRanking(),
            ),
            platformRank = DanggnRankingViewModel.RankingItem.PlatformRanking(
                memberId = "Android",
                text = "1위", totalShakeScore = 120,
            ),
        )
    }
}
