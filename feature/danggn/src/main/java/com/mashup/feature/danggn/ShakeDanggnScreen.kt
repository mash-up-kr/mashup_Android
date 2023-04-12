package com.mashup.feature.danggn

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mashup.core.ui.colors.Gray100
import com.mashup.core.ui.widget.MashUpToolbar
import com.mashup.feature.danggn.ranking.DanggnRankingContent
import com.mashup.feature.danggn.shake.DanggnShakeContent
import com.mashup.core.common.R as CR

@Composable
fun ShakeDanggnScreen(
    modifier: Modifier = Modifier,
    onClickBackButton: () -> Unit,
    onClickDanggnGuideButton: () -> Unit,
) {
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

        // 중간 Divider
        Divider(
            color = Gray100,
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
        )

        // 당근 흔들기 랭킹 UI
        DanggnRankingContent()
    }
}