package com.mashup.feature.danggn.round

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mashup.core.ui.colors.Brand500
import com.mashup.core.ui.colors.Gray500
import com.mashup.core.ui.colors.Gray800
import com.mashup.core.ui.colors.White
import com.mashup.core.ui.extenstions.noRippleClickable
import com.mashup.core.ui.typography.Body3
import com.mashup.core.ui.typography.Body5
import com.mashup.core.ui.widget.bottomsheet.BottomSheetHandler
import com.mashup.core.ui.widget.bottomsheet.BottomSheetTitle
import com.mashup.feature.danggn.ranking.DanggnRankingViewModel
import com.mashup.core.common.R as CR

@Composable
fun DanggnRoundScreen(
    viewModel: DanggnRankingViewModel,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit
) {
    val rounds by viewModel.allDanggnRoundList.collectAsState(emptyList())
    val currentRoundId by viewModel.currentRoundId.collectAsState(0)

    DanggnRoundContent(
        modifier = modifier,
        rounds = rounds,
        currentSelectedRoundId = currentRoundId,
        onRoundSelected = {
            viewModel.updateCurrentRoundId(it)
            onDismiss()
        }
    )
}

@Composable
fun DanggnRoundContent(
    rounds: List<DanggnRankingViewModel.AllRound>,
    currentSelectedRoundId: Int,
    modifier: Modifier = Modifier,
    onRoundSelected: (roundId: Int) -> Unit = {}
) {
    Column(
        modifier = modifier
            .clip(
                RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
            )
            .background(color = White)
    ) {
        BottomSheetHandler()
        BottomSheetTitle(title = "랭킹 회차")
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            itemsIndexed(rounds, key = { _, round -> round.id }) { index, round ->
                DnaggnRoundItem(
                    round = round,
                    isRunning = round.isRunning,
                    isSelected = currentSelectedRoundId == round.id,
                    modifier = Modifier
                        .fillMaxWidth()
                        .noRippleClickable {
                            onRoundSelected(round.id)
                        }
                )
            }
        }
    }
}

@SuppressLint("SimpleDateFormat")
@Composable
fun DnaggnRoundItem(
    round: DanggnRankingViewModel.AllRound,
    isRunning: Boolean,
    isSelected: Boolean,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier.padding(20.dp)) {
        Text(
            text = "${round.number}회차",
            color = Gray800,
            style = Body3.copy(fontWeight = FontWeight.Bold)
        )

        Row(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp)
        ) {
            Text(
                text = "${round.startDate} ~ ${round.endDate}",
                color = Gray500,
                style = Body5
            )

            if (isRunning) {
                Text(
                    modifier = Modifier.padding(start = 8.dp),
                    text = "진행중",
                    style = Body3,
                    color = Brand500
                )
            }
        }

        if (isSelected) {
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(CR.drawable.ic_check),
                contentDescription = null,
                tint = Brand500
            )
        }
    }
}
