package com.mashup.ui.mypage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mashup.databinding.*
import com.mashup.ui.model.AttendanceModel

class AttendanceListAdapter :
    ListAdapter<AttendanceModel, RecyclerView.ViewHolder>(AttendanceComparator) {

    companion object {
        private const val VIEW_TYPE_TITLE = 0
        private const val VIEW_TYPE_SCORE = 1
        private const val VIEW_TYPE_LIST_NONE = 2
        private const val VIEW_TYPE_LIST_ITEM = 3
        private const val VIEW_TYPE_LIST_LEVEL = 4
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).type
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {

            VIEW_TYPE_LIST_ITEM -> {
                MyPageListItemViewHolder(parent)
            }
            VIEW_TYPE_LIST_LEVEL -> {
                MyPageListLevelViewHolder(parent)
            }
            VIEW_TYPE_TITLE -> {
                MyPageTitleViewHolder(parent, mListener)
            }
            VIEW_TYPE_SCORE -> {
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
            LayoutInflater.from(view.context), view, false
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
            LayoutInflater.from(view.context), view, false
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
            LayoutInflater.from(view.context), view, false
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
                LayoutInflater.from(view.context), view, false
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
                LayoutInflater.from(view.context), view, false
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