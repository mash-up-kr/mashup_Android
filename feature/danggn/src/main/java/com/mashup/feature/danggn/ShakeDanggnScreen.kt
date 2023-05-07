package com.mashup.feature.danggn

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mashup.core.ui.colors.Gray100
import com.mashup.core.ui.widget.MashUpToolbar
import com.mashup.feature.danggn.ranking.DanggnRankingContent
import com.mashup.feature.danggn.ranking.DanggnRankingViewModel
import com.mashup.feature.danggn.shake.DanggnShakeContent
import com.mashup.feature.danggn.shake.DanggnShakeEffect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import com.mashup.core.common.R as CR

@Composable
fun ShakeDanggnScreen(
    modifier: Modifier = Modifier,
    viewModel: DanggnViewModel,
    rankingViewModel: DanggnRankingViewModel,
    onClickBackButton: () -> Unit,
    onClickDanggnInfoButton: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()
    val danggnMode by viewModel.danggnMode.collectAsState()
    val feverTimeCountDown by viewModel.feverTimeCountDown.collectAsState()

    val rankUiState by rankingViewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.startDanggnGame()

        launch {
            viewModel.onSuccessAddScore.collectLatest {
                rankingViewModel.getRankingData()
            }
        }
    }

    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize()) {
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

            // 당근 흔들기 UI
            DanggnShakeContent(viewModel = viewModel)

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
                }
            )
        }

        // Shake Effect 영역
        DanggnShakeEffect(
            modifier = Modifier.fillMaxSize(),
            danggnMode = danggnMode,
            countDown = feverTimeCountDown,
            effectList = (uiState as? DanggnUiState.Success)?.danggnGameState?.danggnScoreModelList
                ?: emptyList(),
        )
    }
}
