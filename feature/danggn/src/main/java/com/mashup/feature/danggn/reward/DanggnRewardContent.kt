package com.mashup.feature.danggn.reward

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mashup.core.ui.colors.Gray200
import com.mashup.core.ui.colors.Gray50
import com.mashup.core.ui.colors.Gray700
import com.mashup.core.ui.colors.Gray950
import com.mashup.core.ui.extenstions.noRippleClickable
import com.mashup.core.ui.theme.MashUpTheme
import com.mashup.core.ui.typography.Body3
import com.mashup.feature.danggn.data.dto.DanggnRankingSingleRoundCheckResponse
import com.mashup.feature.danggn.data.dto.DanggnRankingSingleRoundCheckResponse.DanggnRankingReward.DanggnRankingRewardStatus as DanggnRewardStatus

@Composable
fun DanggnRewardContent(
    reward: DanggnRankingSingleRoundCheckResponse.DanggnRankingReward,
    modifier: Modifier = Modifier,
    onClickReward: (rewardId: Int) -> Unit = {}
) {
    val shape = RoundedCornerShape(8.dp)

    when (
        DanggnRewardStatus.getDanggnRankingRewardStatus(reward.status)
    ) {
        DanggnRewardStatus.FIRST_PLACE_MEMBER_NOT_REGISTERED -> {
            if (reward.isFirstPlaceMember) {
                Row(
                    modifier = modifier
                        .background(
                            shape = shape,
                            color = Gray200
                        )
                        .noRippleClickable(onClick = { onClickReward(reward.id ?: return@noRippleClickable) })
                        .padding(horizontal = 14.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "1등 리워드인 전체 공지 한마디를 작성해주세요!",
                        style = Body3,
                        color = Gray700
                    )

                    Icon(
                        painter = painterResource(com.mashup.core.common.R.drawable.ic_chevron_right),
                        tint = Gray50,
                        contentDescription = null
                    )
                }
            }
        }

        DanggnRewardStatus.FIRST_PLACE_MEMBER_REGISTERED -> {
            Box(
                modifier = modifier
                    .background(
                        shape = shape,
                        color = Gray950
                    )
                    .padding(horizontal = 14.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "${reward.name} : ${reward.comment}",
                    style = Body3,
                    color = Color.White
                )
            }
        }

        else -> {
        }
    }
}

@Preview
@Composable
private fun RewardNotRegisteredPrev() {
    MashUpTheme {
        DanggnRewardContent(
            modifier = Modifier.fillMaxWidth(),
            reward = DanggnRankingSingleRoundCheckResponse.DanggnRankingReward(
                status = "FIRST_PLACE_MEMBER_NOT_REGISTERED",
                id = null,
                name = null,
                comment = null,
                isFirstPlaceMember = true
            )
        )
    }
}

@Preview
@Composable
private fun RewardRegisteredPrev() {
    MashUpTheme {
        DanggnRewardContent(
            modifier = Modifier.fillMaxWidth(),
            reward = DanggnRankingSingleRoundCheckResponse.DanggnRankingReward(
                status = "FIRST_PLACE_MEMBER_REGISTERED",
                id = 0,
                name = "김매숑",
                comment = "내가 1위 해버렸쥬? 프디팀 짱이죠",
                isFirstPlaceMember = true
            )
        )
    }
}