package com.mashup.ui.mypage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.mashup.databinding.ItemMypageAttendanceHistoryLevelBinding
import com.mashup.databinding.ItemMypageAttendanceHistoryListBinding
import com.mashup.databinding.ItemMypageAttendanceHistoryPlaceholderEmpthyBinding
import com.mashup.databinding.ItemMypageAttendanceScoreBinding
import com.mashup.databinding.ItemMypageAttendanceTitleBinding
import com.mashup.databinding.ItemMypageProfileBinding
import com.mashup.ui.model.AttendanceModel
import com.mashup.ui.mypage.viewholder.MyPageBaseViewHolder
import com.mashup.ui.mypage.viewholder.MyPageListItemViewHolder
import com.mashup.ui.mypage.viewholder.MyPageListLevelViewHolder
import com.mashup.ui.mypage.viewholder.MyPageListNoneViewHolder
import com.mashup.ui.mypage.viewholder.MyPageProfileViewHolder
import com.mashup.ui.mypage.viewholder.MyPageScoreViewHolder
import com.mashup.ui.mypage.viewholder.MyPageTitleViewHolder

@OptIn(ExperimentalPagerApi::class)
class AttendanceListAdapter
    : ListAdapter<AttendanceModel, MyPageBaseViewHolder>(AttendanceComparator) {

    private val pagerState = PagerState()

    override fun getItemViewType(position: Int): Int {
        return getItem(position).myPageType.type
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyPageBaseViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            MyPageAdapterType.LIST_ITEM.type -> MyPageListItemViewHolder(
                ItemMypageAttendanceHistoryListBinding.inflate(layoutInflater, parent, false)
            )
            MyPageAdapterType.LIST_LEVEL.type -> MyPageListLevelViewHolder(
                ItemMypageAttendanceHistoryLevelBinding.inflate(layoutInflater, parent, false)
            )
            MyPageAdapterType.TITLE.type -> MyPageTitleViewHolder(
                ItemMypageAttendanceTitleBinding.inflate(layoutInflater, parent, false),
                mListener
            )
            MyPageAdapterType.SCORE.type -> MyPageScoreViewHolder(
                ItemMypageAttendanceScoreBinding.inflate(layoutInflater, parent, false)
            )
            MyPageAdapterType.MY_PROFILE.type -> MyPageProfileViewHolder(
                ItemMypageProfileBinding.inflate(layoutInflater, parent, false),
                mListener
            )
            MyPageAdapterType.PROFILE_CARD.type -> MyPageProfileCardViewHolder(
                ComposeView(parent.context),
                pagerState,
            )
            else -> MyPageListNoneViewHolder(
                ItemMypageAttendanceHistoryPlaceholderEmpthyBinding.inflate(layoutInflater, parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: MyPageBaseViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    interface OnItemEventListener {
        fun onTotalAttendanceClick()
        fun onStartSettingActivity()
        fun onStartEditProfileActivity()
    }

    private var mListener: OnItemEventListener? = null

    fun setOnItemClickListener(listener: OnItemEventListener?) {
        mListener = listener
    }
}

object AttendanceComparator : DiffUtil.ItemCallback<AttendanceModel>() {
    override fun areItemsTheSame(
        oldItem: AttendanceModel,
        newItem: AttendanceModel
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: AttendanceModel,
        newItem: AttendanceModel
    ): Boolean {
        return oldItem == newItem
    }
}
