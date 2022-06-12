package com.mashup.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mashup.databinding.DialogBaseBottomSheetBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

abstract class BaseBottomSheetDialogFragment<V : ViewDataBinding> : BottomSheetDialogFragment() {
    private var _rootViewBinding: DialogBaseBottomSheetBinding? = null
    private val rootViewBinding: DialogBaseBottomSheetBinding
        get() = _rootViewBinding!!

    private var _childViewBinding: V? = null
    protected val viewBinding: V
        get() = _childViewBinding!!

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
        _rootViewBinding = DialogBaseBottomSheetBinding.inflate(
            LayoutInflater.from(context), null, false
        )
        _childViewBinding =
            DataBindingUtil.inflate<V>(
                LayoutInflater.from(requireContext()), layoutId, rootViewBinding.content, true
            )
        rootViewBinding.lifecycleOwner = this
        return rootViewBinding.root
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
        _rootViewBinding = null
        _childViewBinding = null
    }

    protected fun setTitle(title: String) {
        rootViewBinding.title.text = title
    }

    protected fun expendBottomSheet() {
        behavior?.state = BottomSheetBehavior.STATE_EXPANDED
    }

    protected fun hideBottomSheet() {
        behavior?.state = BottomSheetBehavior.STATE_HIDDEN
    }

    protected fun flowLifecycleScope(
        state: Lifecycle.State = Lifecycle.State.STARTED,
        block: suspend CoroutineScope.() -> Unit
    ) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(state) {
                block.invoke(this)
            }
        }
    }
}