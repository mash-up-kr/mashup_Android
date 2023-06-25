package com.mashup.feature.setting.ui.push

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mashup.core.common.R
import com.mashup.datastore.model.UserPreference
import com.mashup.core.ui.widget.MashUpToolbar

@Composable
fun PushScreen(
    userPreference: com.mashup.datastore.model.UserPreference,
    modifier: Modifier = Modifier,
    onClickBackButton: () -> Unit = {},
    onToggleMashUpPush: (Boolean) -> Unit = {},
    onToggleDanggnPush: (Boolean) -> Unit = {},
) {
    PushContent(
        modifier = modifier,
        isSelectedMashUpPush = userPreference.pushNotificationAgreed,
        isSelectedDanggnPush = userPreference.danggnPushNotificationAgreed,
        onClickBackButton = onClickBackButton,
        onToggleMashupPush = onToggleMashUpPush,
        onToggleDanggnPush = onToggleDanggnPush
    )
}

@Composable
fun PushContent(
    isSelectedMashUpPush: Boolean,
    isSelectedDanggnPush: Boolean,
    modifier: Modifier = Modifier,
    onClickBackButton: () -> Unit = {},
    onToggleMashupPush: (Boolean) -> Unit = {},
    onToggleDanggnPush: (Boolean) -> Unit = {},
) {
    Column(modifier = modifier) {
        MashUpToolbar(
            title = "알림",
            showBackButton = true,
            onClickBackButton = onClickBackButton
        )

        TogglePushItem(
            title = "매시업 소식 알림",
            titleColorRes = R.color.gray800,
            description = "출석 시간과 세미나 일정, 그리고 활동 점수 \n업데이트 소식을 받을 수 있어요",
            descriptionRes = R.color.gray500,
            onCheckedChange = onToggleMashupPush,
            checked = isSelectedMashUpPush
        )

        TogglePushItem(
            title = "당근 흔들기 알림",
            titleColorRes = R.color.gray800,
            description = "당근 흔들기 랭킹과 누적 횟수 달성 \n업데이트 소식을 받을 수 있어요",
            descriptionRes = R.color.gray500,
            onCheckedChange = onToggleDanggnPush,
            checked = isSelectedDanggnPush
        )
    }
}