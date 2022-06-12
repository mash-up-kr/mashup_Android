package com.mashup.ui.signup.dialog.platform

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.mashup.R
import com.mashup.databinding.ItemPlatformBinding

class PlatformAdapter(
    private val platformList: List<String>,
    private val clickListener: (String) -> Unit
) : RecyclerView.Adapter<PlatformViewHolder>() {

    private var selectedPlatform: String? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlatformViewHolder {
        return PlatformViewHolder(
            ItemPlatformBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            clickListener
        )
    }

    override fun onBindViewHolder(holder: PlatformViewHolder, position: Int) {
        holder.onBindContent(platformList[position], selectedPlatform)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateSelectedPlatform(platform: String) {
        selectedPlatform = platform
        notifyDataSetChanged()
    }

    override fun getItemCount() = platformList.size
}

class PlatformViewHolder(
    private val binding: ItemPlatformBinding,
    private val clickListener: (String) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    private var platform: String? = null

    init {
        binding.root.setOnClickListener {
            platform?.run {
                clickListener(this)
            }
        }
    }

    fun onBindContent(
        platform: String, selectedPlatform: String?
    ) = with(binding) {
        this@PlatformViewHolder.platform = platform
        tvPlatform.text = platform

        if (platform == selectedPlatform) {
            onBindSelectedContent()
        } else {
            onBindUnSelectedContent()
        }
    }

    private fun onBindSelectedContent() = with(binding) {
        root.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.primary100))
        tvPlatform.setTextColor(ContextCompat.getColor(itemView.context, R.color.primary600))
        imgChecked.isVisible = true
    }

    private fun onBindUnSelectedContent() = with(binding) {
        root.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.white))
        tvPlatform.setTextColor(ContextCompat.getColor(itemView.context, R.color.gray800))
        imgChecked.isVisible = false
    }
}