package com.mashup.core.common.widget

import android.content.Context
import android.view.View
import androidx.core.view.WindowCompat
import com.google.android.material.bottomsheet.BottomSheetDialog
class EdgeToEdgeBottomSheetDialog(
    context: Context,
    theme: Int
) : BottomSheetDialog(context, theme) {
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        window?.let {
            WindowCompat.setDecorFitsSystemWindows(it, false)
        }

        findViewById<View>(com.google.android.material.R.id.container)?.apply {
            fitsSystemWindows = false
        }

        findViewById<View>(com.google.android.material.R.id.coordinator)?.apply {
            fitsSystemWindows = false
        }
    }
}
