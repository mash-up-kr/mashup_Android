package com.mashup.ui.signin.dialog

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.mashup.R
import com.mashup.base.BaseBottomSheetDialogFragment
import com.mashup.databinding.DialogPlatformSelectionBinding
import com.mashup.databinding.ItemPlatformBinding
import com.mashup.ui.signin.SignInViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlatFormSelectionDialog : BaseBottomSheetDialogFragment<DialogPlatformSelectionBinding>() {

    private val viewModel: SignInViewModel by activityViewModels()
    private val adapter: PlatformAdapter = PlatformAdapter(platformList) { platform ->
        viewModel.setPlatform(platform)
        dismiss()
    }

    override fun initViews() {
        setTitle("플랫폼 선택")
        initPlatFormRecyclerView()
    }

    private fun initPlatFormRecyclerView() {
        viewBinding.rvPlatform.adapter = adapter
    }

    override val layoutId: Int
        get() = R.layout.dialog_platform_selection

    companion object {
        private val platformList = listOf(
            "Product Design", "Android", "iOS", "Web", "Spring", "Node"
        )
    }
}

class PlatformAdapter(
    private val platformList: List<String>,
    private val clickListener: (String) -> Unit
) : RecyclerView.Adapter<PlatformAdapter.PlatformViewHolder>() {

    class PlatformViewHolder(
        val binding: ItemPlatformBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlatformViewHolder {
        return PlatformViewHolder(
            ItemPlatformBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: PlatformViewHolder, position: Int) {
        with(holder.binding) {
            tvPlatform.text = platformList[position]
            if (!root.hasOnClickListeners()) {
                root.setOnClickListener {
                    clickListener(platformList[position])
                }
            }
        }
    }

    override fun getItemCount() = platformList.size
}