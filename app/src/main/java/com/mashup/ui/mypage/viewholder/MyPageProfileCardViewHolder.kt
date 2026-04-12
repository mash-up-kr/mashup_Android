package com.mashup.ui.mypage.viewholder

import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import com.mashup.core.ui.theme.MashUpTheme
import com.mashup.feature.mypage.profile.card.MyPageProfileCardScreen
import com.mashup.ui.model.AttendanceModel
import com.mashup.ui.mypage.AttendanceListAdapter

class MyPageProfileCardViewHolder constructor(
    private val composeView: ComposeView,
    private val listener: AttendanceListAdapter.OnItemEventListener?
) : MyPageBaseViewHolder(composeView) {

    init {
        composeView.setViewCompositionStrategy(
            ViewCompositionStrategy.DisposeOnDetachedFromWindowOrReleasedFromPool // (Default)
        )
    }

    override fun bind(item: AttendanceModel) {
        val cards = (item as AttendanceModel.ProfileCard).cardList

        composeView.setContent {
            val pagerState = rememberPagerState(pageCount = { cards.size })
            MashUpTheme {
                MyPageProfileCardScreen(cards, pagerState) {
                    listener?.onStartEditProfileCardActivity(it)
                }
            }
        }
    }
}
