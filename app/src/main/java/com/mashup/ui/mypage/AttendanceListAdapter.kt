package com.mashup.ui.mypage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mashup.R
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
                MyPageListItemViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.item_mypage_attendance_history_list, parent, false
                    )
                )
            }
            VIEW_TYPE_LIST_LEVEL -> {
                MyPageListLevelViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.item_mypage_attendance_history_level, parent, false
                    )
                )
            }
            VIEW_TYPE_TITLE -> {
                MyPageTitleViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.item_mypage_attendance_title, parent, false
                    )
                )
            }
            VIEW_TYPE_SCORE -> {
                MyPageScoreViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.item_mypage_attendance_score, parent, false
                    ),
                    mListener
                )
            }
            else -> {
                MyPageListNoneViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.item_mypage_attendance_history_placeholder_empthy, parent, false
                    )
                )
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

    class MyPageListItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding: ItemMypageAttendanceHistoryListBinding? =
            androidx.databinding.DataBindingUtil.bind(itemView)

        fun bind(item: AttendanceModel) {
            binding?.model = item
        }
    }

    class MyPageListLevelViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding: ItemMypageAttendanceHistoryLevelBinding? =
            androidx.databinding.DataBindingUtil.bind(itemView)

        fun bind(item: AttendanceModel) {
            binding?.model = item
        }
    }

    class MyPageListNoneViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding: ItemMypageAttendanceHistoryPlaceholderEmpthyBinding? =
            androidx.databinding.DataBindingUtil.bind(itemView)

        fun bind(item: AttendanceModel) {
            binding?.model = item
        }
    }

    class MyPageScoreViewHolder(view: View, val listener: OnItemEventListener?) :
        RecyclerView.ViewHolder(view) {
        private val binding: ItemMypageAttendanceScoreBinding? =
            androidx.databinding.DataBindingUtil.bind(itemView)

        fun bind(item: AttendanceModel) {
            binding?.let {
                it.model = item
                it.layoutTotalAttendance.setOnClickListener {
                    listener?.onTotalAttendanceClick()
                }
            }
        }
    }

    class MyPageTitleViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding: ItemMypageAttendanceTitleBinding? =
            androidx.databinding.DataBindingUtil.bind(itemView)

        fun bind(item: AttendanceModel) {
            binding?.model = item
        }
    }


    interface OnItemEventListener {
        fun onTotalAttendanceClick()
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