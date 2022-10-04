package com.mashup.ui.signup.dialog.platform

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.mashup.R
import com.mashup.databinding.ItemPlatformBinding
import com.mashup.core.model.Platform

class PlatformAdapter(
    private val platformList: List<Platform>,
    private val clickListener: (Platform) -> Unit
) : RecyclerView.Adapter<PlatformViewHolder>() {

    private var selectedPlatform: Platform? = null

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
    fun updateSelectedPlatform(platform: Platform) {
        selectedPlatform = platform
        notifyDataSetChanged()
    }

    override fun getItemCount() = platformList.size
}

class PlatformViewHolder(
    private val binding: ItemPlatformBinding,
    private val clickListener: (Platform) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    private var platform: Platform? = null

    init {
        binding.root.setOnClickListener {
            platform?.run {
                clickListener(this)
            }
        }
    }

    fun onBindContent(
        platform: Platform,
        selectedPlatform: Platform?
    ) = with(binding) {
        this@PlatformViewHolder.platform = platform
        tvPlatform.text = platform.detailName

        if (platform == selectedPlatform) {
            onBindSelectedContent()
        } else {
            onBindUnSelectedContent()
        }
    }

    private fun onBindSelectedContent() = with(binding) {
        root.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.brand100))
        tvPlatform.setTextColor(ContextCompat.getColor(itemView.context, R.color.brand600))
        imgChecked.isVisible = true
    }

    private fun onBindUnSelectedContent() = with(binding) {
        root.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.white))
        tvPlatform.setTextColor(ContextCompat.getColor(itemView.context, R.color.gray800))
        imgChecked.isVisible = false
    }
}
