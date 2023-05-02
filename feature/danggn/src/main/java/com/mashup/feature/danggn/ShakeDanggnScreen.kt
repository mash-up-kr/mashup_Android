package com.mashup.feature.danggn

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.mashup.core.ui.colors.Gray100
import com.mashup.core.ui.widget.MashUpToolbar
import com.mashup.feature.danggn.data.danggn.GoldenDanggnMode
import com.mashup.feature.danggn.ranking.DanggnRankingContent
import com.mashup.feature.danggn.ranking.DanggnRankingViewModel
import com.mashup.feature.danggn.shake.DanggnShakeContent
import com.mashup.core.common.R as CR

@Composable
fun ShakeDanggnScreen(
    modifier: Modifier = Modifier,
    viewModel: DanggnViewModel,
    rankingViewModel: DanggnRankingViewModel,
    onClickBackButton: () -> Unit,
    onClickDanggnInfoButton: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState(DanggnUiState.Loading)
    val danggnMode by viewModel.danggnMode.collectAsState()
    val uiRankState by rankingViewModel.mashUpRankingList.collectAsState()
    val personalRankState by rankingViewModel.personalRanking.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.startDanggnGame()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = modifier
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
                allRankList = uiRankState.sortedByDescending { it.totalShakeScore },
                personalRank = personalRankState
            )
        }

        // Shake Effect 영역
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(if (danggnMode is GoldenDanggnMode) Color(0xCC000000) else Color.Transparent)
        ) {
            if (danggnMode is GoldenDanggnMode) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 50.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = CR.drawable.img_fevertime_title),
                        contentDescription = null,
                        modifier = Modifier.width(300.dp)
                    )
                }

                Image(
                    painter = painterResource(id = CR.drawable.img_fever_danggn),
                    contentDescription = null,
                    modifier = Modifier
                        .size(174.dp)
                        .align(Alignment.Center)
                )
            }
        }
    }
}
