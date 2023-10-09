package com.mashup.ui.mypage.viewholder

import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.mashup.core.ui.theme.MashUpTheme
import com.mashup.feature.mypage.profile.card.MyPageProfileCardScreen
import com.mashup.ui.model.AttendanceModel
import com.mashup.ui.mypage.AttendanceListAdapter

@OptIn(ExperimentalPagerApi::class)
class MyPageProfileCardViewHolder constructor(
    private val composeView: ComposeView,
    private val pagerState: PagerState,
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
            MashUpTheme {
                MyPageProfileCardScreen(cards, pagerState) {
                    listener?.onStartEditProfileCardActivity(it)
                }
            }
        }
    }
}
