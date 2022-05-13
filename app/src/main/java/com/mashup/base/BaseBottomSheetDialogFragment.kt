package com.mashup.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class BaseBottomSheetDialogFragment<V : ViewDataBinding> : BottomSheetDialogFragment() {
    private var _viewBinding: V? = null
    protected val viewBinding: V
        get() = _viewBinding!!

    abstract val layoutId: Int

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
    ): View? {
        _viewBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(requireContext()), layoutId, null, false
            )
        viewBinding.lifecycleOwner = viewLifecycleOwner
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initObserves()

        behavior?.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                onStateChangedBottomSheet(bottomSheet, newState)
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                onSlideStateBottomSheet(bottomSheet, slideOffset)
            }
        }
        )
    }

    open fun initViews() {
        /* explicitly empty */
    }

    open fun initObserves() {
        /* explicitly empty */
    }

    open fun onStateChangedBottomSheet(bottomSheet: View, newState: Int) {
        /* explicitly empty */
    }

    open fun onSlideStateBottomSheet(bottomSheet: View, slideOffset: Float) {
        /* explicitly empty */
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }

    protected fun expendBottomSheet() {
        behavior?.state = BottomSheetBehavior.STATE_EXPANDED
    }

    protected fun hideBottomSheet() {
        behavior?.state = BottomSheetBehavior.STATE_HIDDEN
    }
}