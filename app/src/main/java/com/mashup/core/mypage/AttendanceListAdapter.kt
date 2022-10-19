package com.mashup.core.mypage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mashup.databinding.ItemMypageAttendanceHistoryLevelBinding
import com.mashup.databinding.ItemMypageAttendanceHistoryListBinding
import com.mashup.databinding.ItemMypageAttendanceHistoryPlaceholderEmpthyBinding
import com.mashup.databinding.ItemMypageAttendanceScoreBinding
import com.mashup.databinding.ItemMypageAttendanceTitleBinding
import com.mashup.core.model.AttendanceModel

class AttendanceListAdapter :
    ListAdapter<AttendanceModel, RecyclerView.ViewHolder>(AttendanceComparator) {

    override fun getItemViewType(position: Int): Int {
        return getItem(position).myPageType.type
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            MyPageAdapterType.LIST_ITEM.type -> {
                MyPageListItemViewHolder(parent)
            }
            MyPageAdapterType.LIST_LEVEL.type -> {
                MyPageListLevelViewHolder(parent)
            }
            MyPageAdapterType.TITLE.type -> {
                MyPageTitleViewHolder(parent, mListener)
            }
            MyPageAdapterType.SCORE.type -> {
                MyPageScoreViewHolder(parent, mListener)
            }
            else -> {
                MyPageListNoneViewHolder(parent)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is MyPageListItemViewHolder -> {
                holder.bind(getItem(position))
            }
            is MyPageListLevelViewHolder -> {
                holder.bind(getItem(position))
            }
            is MyPageListNoneViewHolder -> {
                holder.bind(getItem(position))
            }
            is MyPageScoreViewHolder -> {
                holder.bind(getItem(position))
            }
            is MyPageTitleViewHolder -> {
                holder.bind(getItem(position))
            }
        }
    }

    class MyPageListItemViewHolder(view: ViewGroup) : RecyclerView.ViewHolder(
        ItemMypageAttendanceHistoryListBinding.inflate(
            LayoutInflater.from(view.context),
            view,
            false
        ).root
    ) {
        private val binding: ItemMypageAttendanceHistoryListBinding? =
            androidx.databinding.DataBindingUtil.bind(itemView)

        fun bind(item: AttendanceModel) {
            binding?.model = item
        }
    }

    class MyPageListLevelViewHolder(view: ViewGroup) : RecyclerView.ViewHolder(
        ItemMypageAttendanceHistoryLevelBinding.inflate(
            LayoutInflater.from(view.context),
            view,
            false
        ).root
    ) {
        private val binding: ItemMypageAttendanceHistoryLevelBinding? =
            androidx.databinding.DataBindingUtil.bind(itemView)

        fun bind(item: AttendanceModel) {
            binding?.model = item
        }
    }

    class MyPageListNoneViewHolder(view: ViewGroup) : RecyclerView.ViewHolder(
        ItemMypageAttendanceHistoryPlaceholderEmpthyBinding.inflate(
            LayoutInflater.from(view.context),
            view,
            false
        ).root
    ) {
        private val binding: ItemMypageAttendanceHistoryPlaceholderEmpthyBinding? =
            androidx.databinding.DataBindingUtil.bind(itemView)

        fun bind(item: AttendanceModel) {
            binding?.model = item
        }
    }

    class MyPageScoreViewHolder(view: ViewGroup, val listener: OnItemEventListener?) :
        RecyclerView.ViewHolder(
            ItemMypageAttendanceScoreBinding.inflate(
                LayoutInflater.from(view.context),
                view,
                false
            ).root
        ) {
        private val binding: ItemMypageAttendanceScoreBinding? =
            androidx.databinding.DataBindingUtil.bind(itemView)

        fun bind(item: AttendanceModel) {
            binding?.let {
                it.model = item
                it.btnDetail.setOnClickListener {
                    listener?.onTotalAttendanceClick()
                }
            }
        }
    }

    class MyPageTitleViewHolder(view: ViewGroup, val listener: OnItemEventListener?) :
        RecyclerView.ViewHolder(
            ItemMypageAttendanceTitleBinding.inflate(
                LayoutInflater.from(view.context),
                view,
                false
            ).root
        ) {
        private val binding: ItemMypageAttendanceTitleBinding? =
            androidx.databinding.DataBindingUtil.bind(itemView)

        fun bind(item: AttendanceModel) {
            binding?.let {
                it.model = item
                it.btnSetting.setOnClickListener {
                    listener?.onStartSettingActivity()
                }
            }
            binding?.model = item
        }
    }

    interface OnItemEventListener {
        fun onTotalAttendanceClick()
        fun onStartSettingActivity()
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
