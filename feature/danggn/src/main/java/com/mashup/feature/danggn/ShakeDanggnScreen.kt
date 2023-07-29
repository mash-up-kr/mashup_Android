package com.mashup.feature.danggn

import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.runtime.*
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
import com.mashup.core.common.utils.safeShow
import com.mashup.core.ui.colors.Gray100
import com.mashup.core.ui.widget.MashUpToolbar
import com.mashup.feature.danggn.data.danggn.GoldenDanggnMode
import com.mashup.feature.danggn.ranking.DanggnRankingContent
import com.mashup.feature.danggn.ranking.DanggnRankingViewModel
import com.mashup.feature.danggn.ranking.DanggnRankingViewModel.FirstRankingState.*
import com.mashup.feature.danggn.ranking.DanggnWeeklyRankingContent
import com.mashup.feature.danggn.reward.DanggnFirstPlaceBottomPopup
import com.mashup.feature.danggn.reward.DanggnRewardContent
import com.mashup.feature.danggn.reward.DanggnRewardNoticeScreen
import com.mashup.feature.danggn.shake.DanggnShakeContent
import com.mashup.feature.danggn.shake.DanggnShakeEffect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import com.mashup.core.common.R as CR
import com.mashup.feature.danggn.data.dto.DanggnRankingSingleRoundCheckResponse.DanggnRankingReward.DanggnRankingRewardStatus.FIRST_PLACE_MEMBER_REGISTERED as DANGGN_REWARD_REGISTERED

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ShakeDanggnScreen(
    viewModel: DanggnViewModel,
    rankingViewModel: DanggnRankingViewModel,
    modifier: Modifier = Modifier,
    onClickBackButton: () -> Unit = {},
    onClickDanggnInfoButton: () -> Unit = {},
    onClickHelpButton: () -> Unit = {},
    onClickAnotherRounds: () -> Unit = {},
    onClickReward: (rewardId: Int) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val randomTodayMessage by viewModel.randomMessage.collectAsState()
    val danggnMode by viewModel.danggnMode.collectAsState()
    val rankUiState by rankingViewModel.uiState.collectAsState()
    val context = LocalContext.current
    val danggnRound by rankingViewModel.currentRound.collectAsState()
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    val isRefreshing = rankingViewModel.isRefreshing.collectAsState().value
    val currentRoundId by rankingViewModel.currentRoundId.collectAsState(0)
    val showDanggnRewardDialog by rankingViewModel.showRewardNoticeDialog.collectAsState(false)
    val pullRefreshState = rememberSwipeRefreshState(isRefreshing)
    val refreshTriggerDistance = 80.dp
    val showLastRoundRewardPopup by rankingViewModel.showLastRoundRewardPopup.collectAsState(null)

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

                Box(modifier = Modifier.fillMaxWidth()) {
                    danggnRound?.danggnRankingReward?.let { reward ->
                        DanggnRewardContent(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp),
                            reward = reward,
                            onClickReward = onClickReward
                        )
                    }
                    // 당근 흔들기 UI
                    DanggnShakeContent(
                        randomTodayMessage = randomTodayMessage,
                        danggnMode = danggnMode
                    )
                }

                // 중간 Divider
                Divider(
                    color = Gray100,
                    modifier = Modifier.fillMaxWidth(),
                    thickness = 4.dp
                )

                (rankUiState.danggnAllRoundList.find { it.id == currentRoundId })?.let { round ->
                    // 당근 회차 알리미
                    DanggnWeeklyRankingContent(
                        round = round,
                        timerCount = rankUiState.timer.timerString,
                        onClickAnotherRounds = onClickAnotherRounds,
                        onClickHelpButton = onClickHelpButton
                    )
                }

                // 당근 흔들기 랭킹 UI
                DanggnRankingContent(
                    personalRankList = rankUiState.personalRankingList.sortedByDescending { it.totalShakeScore },
                    myPersonalRank = rankUiState.myPersonalRanking,
                    platformRankList = rankUiState.platformRankingList.sortedByDescending { it.totalShakeScore },
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

            when (val state = rankUiState.firstPlaceState) {
                is FirstRanking -> {
                    DanggnFirstPlaceScreen(
                        name = state.text,
                        onClickCloseButton = {
                            rankingViewModel.updateFirstRanking()
                        }
                    )
                }

                Empty -> {

                }
            }

            if (showDanggnRewardDialog && danggnRound?.danggnRankingReward?.status == DANGGN_REWARD_REGISTERED.name) {
                DanggnRewardNoticeScreen(
                    modifier = Modifier.fillMaxSize(),
                    name = danggnRound?.danggnRankingReward?.name.orEmpty(),
                    message = danggnRound?.danggnRankingReward?.comment.orEmpty(),
                    onClickCloseButton = rankingViewModel::confirmDanggnRewardNotice
                )
            }

            showLastRoundRewardPopup?.let {
                DanggnLastRoundFirstPlaceScreen(
                    name = it.first,
                    onClickCloseButton = {
                        rankingViewModel.dismissLastRoundFirstPlacePopup()
                        DanggnFirstPlaceBottomPopup
                            .getNewInstance(
                                it.second,
                                rankingViewModel.currentRound.value?.danggnRankingReward?.id
                                    ?: return@DanggnLastRoundFirstPlaceScreen
                            )
                            .safeShow((context as AppCompatActivity).supportFragmentManager)
                    })
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

    Box(
        modifier
            .fillMaxWidth()
            .padding(vertical = 150.dp * progress)
    ) {
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