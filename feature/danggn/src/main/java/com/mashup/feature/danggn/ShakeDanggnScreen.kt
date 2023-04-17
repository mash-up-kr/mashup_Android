package com.mashup.feature.danggn

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mashup.core.ui.colors.Gray100
import com.mashup.core.ui.widget.MashUpToolbar
import com.mashup.feature.danggn.data.DanggnShakerState
import com.mashup.feature.danggn.ranking.DanggnRankingContent
import com.mashup.feature.danggn.shake.DanggnShakeContent
import com.mashup.core.common.R as CR

@Composable
fun ShakeDanggnScreen(
    modifier: Modifier = Modifier,
    viewModel: DanggnViewModel,
    onClickBackButton: () -> Unit,
    onClickDanggnGuideButton: () -> Unit,
) {

    val danggnComboState by viewModel.danggnState.collectAsState(DanggnShakerState.Idle)

    LaunchedEffect(Unit) {
        viewModel.subscribeShakeSensor()
    }

    Column(
        modifier = modifier
    ) {
        MashUpToolbar(
            title = "당근 흔들기",
            showBackButton = true,
            onClickBackButton = onClickBackButton,
            showActionButton = true,
            onClickActionButton = onClickDanggnGuideButton,
            actionButtonDrawableRes = CR.drawable.ic_info
        )

        // 당근 흔들기 UI
        DanggnShakeContent()
        
        Text(
            modifier = Modifier.padding(12.dp),
            text = danggnComboState.toString()
        )

        // 중간 Divider
        Divider(
            color = Gray100,
            modifier = Modifier.fillMaxWidth(),
            thickness = 4.dp
        )

        // 당근 흔들기 랭킹 UI
        DanggnRankingContent()
    }
}