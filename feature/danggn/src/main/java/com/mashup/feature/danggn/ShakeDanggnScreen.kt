package com.mashup.feature.danggn

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.mashup.core.common.extensions.haptic
import com.mashup.core.ui.colors.Gray100
import com.mashup.core.ui.widget.MashUpToolbar
import com.mashup.feature.danggn.data.danggn.GoldenDanggnMode
import com.mashup.feature.danggn.ranking.DanggnRankingContent
import com.mashup.feature.danggn.ranking.DanggnRankingViewModel
import com.mashup.feature.danggn.shake.DanggnShakeContent
import com.mashup.feature.danggn.shake.DanggnShakeEffect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import com.mashup.core.common.R as CR

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ShakeDanggnScreen(
    modifier: Modifier = Modifier,
    viewModel: DanggnViewModel,
    rankingViewModel: DanggnRankingViewModel,
    onClickBackButton: () -> Unit,
    onClickDanggnInfoButton: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()
    val randomTodayMessage by viewModel.randomMessage.collectAsState()
    val danggnMode by viewModel.danggnMode.collectAsState()
    val feverTimeCountDown by viewModel.feverTimeCountDown.collectAsState()

    val rankUiState by rankingViewModel.uiState.collectAsState()

    val context = LocalContext.current

    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()

    val isRefreshing = rankingViewModel.isRefreshing.collectAsState().value
    val pullRefreshState = rememberSwipeRefreshState(isRefreshing)
    val refreshTriggerDistance = 80.dp

    LaunchedEffect(Unit) {
        viewModel.startDanggnGame()

        launch {
            viewModel.onSuccessAddScore.collectLatest {
                rankingViewModel.getRankingData()
            }
        }

        launch {
            viewModel.onShakeDevice.collectLatest {
                context.haptic(amplitude = shakeVibrateAmplitude)
                scrollState.scrollTo(0) // 당근 흔들면 화면 스크롤 상단으로 올리기
            }
        }
    }

    LaunchedEffect(danggnMode) {
        if (danggnMode is GoldenDanggnMode) {
            context.haptic(amplitude = goldDanggnModeVibrateAmplitude)
        }
    }

    CompositionLocalProvider(
        LocalOverscrollConfiguration provides null
    ) {
        SwipeRefresh(
            state = pullRefreshState,
            onRefresh = { rankingViewModel.refreshRankingData() },
            modifier = Modifier.fillMaxSize(),
            indicatorAlignment = Alignment.TopCenter,
            indicator = { _, _ -> }
        ) {
            Column(
                modifier = modifier.verticalScroll(scrollState)
            ) {
                MashUpToolbar(
                    title = "당근 흔들기",
                    showBackButton = true,
                    onClickBackButton = onClickBackButton,
                    showActionButton = true,
                    onClickActionButton = onClickDanggnInfoButton,
                    actionButtonDrawableRes = CR.drawable.ic_info
                )

                DanggnPullToRefreshIndicator(
                    pullRefreshState = pullRefreshState,
                    refreshTriggerDistance = refreshTriggerDistance
                )

                // 당근 흔들기 UI
                DanggnShakeContent(
                    randomTodayMessage = randomTodayMessage,
                    danggnMode = danggnMode
                )

                // 중간 Divider
                Divider(
                    color = Gray100,
                    modifier = Modifier.fillMaxWidth(),
                    thickness = 4.dp
                )

                // 당근 흔들기 랭킹 UI
                DanggnRankingContent(
                    allMashUpMemberRankState = rankUiState.personalRankingList.sortedByDescending { it.totalShakeScore },
                    personalRank = rankUiState.myPersonalRanking,
                    allPlatformRank = rankUiState.platformRankingList.sortedByDescending { it.totalShakeScore },
                    platformRank = rankUiState.myPlatformRanking,
                    onClickScrollTopButton = {
                        coroutineScope.launch {
                            scrollState.scrollTo(0)
                        }
                    },
                    onChangedTabIndex = rankingViewModel::updateCurrentTabIndex
                )
            }

            // Shake Effect 영역
            DanggnShakeEffect(
                modifier = Modifier.fillMaxSize(),
                danggnMode = danggnMode,
                effectList = (uiState as? DanggnUiState.Success)?.danggnGameState?.danggnScoreModelList
                    ?: emptyList(),
            )

            (rankUiState.firstPlaceState as? DanggnRankingViewModel.FirstRankingState.FirstRanking)?.run {
                DanggnFirstPlaceScreen(
                    name = text,
                    onClickCloseButton = {
                        rankingViewModel.updateFirstRanking()
                    }
                )
            }
        }
    }
}

@Composable
fun DanggnPullToRefreshIndicator(
    modifier: Modifier = Modifier,
    pullRefreshState: SwipeRefreshState,
    refreshTriggerDistance: Dp
) {
    val trigger = with(LocalDensity.current) { refreshTriggerDistance.toPx() }
    val progress = (pullRefreshState.indicatorOffset / trigger).coerceIn(0f, 1f)

    val animationIndicatorHeight by animateDpAsState(
        targetValue = if (pullRefreshState.isRefreshing && pullRefreshState.isSwipeInProgress.not()) 32.dp else 32.dp * progress,
        animationSpec = tween(
            durationMillis = if (pullRefreshState.isRefreshing) 1 else 200,
            easing = LinearOutSlowInEasing
        )
    )

    Box(modifier.fillMaxWidth().padding(vertical = 150.dp * progress)) {
        Image(
            painter = painterResource(id = com.mashup.core.common.R.drawable.img_carrot_pulltorefresh),
            contentDescription = null,
            modifier = Modifier
                .width(235.dp)
                .height(animationIndicatorHeight)
                .align(Center)
        )
    }
}

private const val shakeVibrateAmplitude = 40
private const val goldDanggnModeVibrateAmplitude = 255