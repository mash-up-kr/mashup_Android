package com.mashup.ui.attendance.member

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mashup.databinding.DialogMemberInfoBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MemberInfoDialog : BottomSheetDialogFragment() {
    private var _binding: DialogMemberInfoBinding? = null
    protected val binding: DialogMemberInfoBinding
        get() = _binding!!

    private val behavior: BottomSheetBehavior<View>?
        get() {
            val bottomSheet =
                dialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            return bottomSheet?.let {
                BottomSheetBehavior.from(bottomSheet)
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogMemberInfoBinding.inflate(inflater)
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bottomSheetDialog = dialog as? BottomSheetDialog
        bottomSheetDialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            ?.run {
                setBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        android.R.color.transparent
                    )
                )
            }

        addGlobalLayoutListener(view)
    }

    private fun addGlobalLayoutListener(view: View) {
        view.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (this@MemberInfoDialog.view?.height == 0) return
                behavior?.state = BottomSheetBehavior.STATE_EXPANDED
                behavior?.peekHeight = this@MemberInfoDialog.view?.height ?: 0
            }
        })
    }

    companion object {
        fun newInstance() = MemberInfoDialog()
    }
}
