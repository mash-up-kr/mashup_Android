package com.mashup.ui.danggn

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mashup.core.common.utils.keyboard.RootViewDeferringInsetsCallback
import com.mashup.core.ui.theme.MashUpTheme
import com.mashup.feature.danggn.ranking.DanggnRankingViewModel
import com.mashup.feature.danggn.round.DanggnRoundScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DanggnRoundSelectorDialog : BottomSheetDialogFragment() {
    private var _composeView: ComposeView? = null
    private val composeView: ComposeView
        get() = _composeView!!

    private val danggnRankingViewModel: DanggnRankingViewModel by activityViewModels()

    companion object {
        fun newInstance() = DanggnRoundSelectorDialog()
    }

    private val behavior: BottomSheetBehavior<View>?
        get() {
            val bottomSheet =
                dialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            return bottomSheet?.let {
                BottomSheetBehavior.from(bottomSheet)
            }
        }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        object : BottomSheetDialog(requireContext(), theme) {
            override fun onAttachedToWindow() {
                super.onAttachedToWindow()

                window?.let {
                    WindowCompat.setDecorFitsSystemWindows(it, false)
                }

                findViewById<View>(com.google.android.material.R.id.container)?.apply {
                    fitsSystemWindows = false
                }

                findViewById<View>(com.google.android.material.R.id.coordinator)?.fitsSystemWindows = false
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _composeView = ComposeView(requireContext())
        return composeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)

            setContent {
                MashUpTheme {
                    DanggnRoundScreen(
                        modifier = Modifier.fillMaxWidth(),
                        viewModel = danggnRankingViewModel,
                        onDismiss = this@DanggnRoundSelectorDialog::dismissAllowingStateLoss
                    )
                }
            }
        }
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

        initWindowInset()

        addGlobalLayoutListener(view)
    }

    private fun initWindowInset() {
        val deferringInsetsListener = RootViewDeferringInsetsCallback(
            persistentInsetTypes = WindowInsetsCompat.Type.navigationBars(),
            deferredInsetTypes = WindowInsetsCompat.Type.ime()
        )

        ViewCompat.setWindowInsetsAnimationCallback(composeView, deferringInsetsListener)
        ViewCompat.setOnApplyWindowInsetsListener(composeView, deferringInsetsListener)
    }

    private fun addGlobalLayoutListener(view: View) {
        view.viewTreeObserver.addOnGlobalLayoutListener(object :
                ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    if (this@DanggnRoundSelectorDialog.view?.height == 0) return
                    behavior?.state = BottomSheetBehavior.STATE_EXPANDED
                    behavior?.peekHeight = this@DanggnRoundSelectorDialog.view?.height ?: 0
                }
            })
    }
}
