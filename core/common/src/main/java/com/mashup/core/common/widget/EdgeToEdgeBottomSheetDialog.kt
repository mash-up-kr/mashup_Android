package com.mashup.core.common.widget

import android.content.Context
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
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
            ViewCompat.setOnApplyWindowInsetsListener(this) { view, insets ->
                val imeHeight = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom
                val navBarHeight = insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom
                view.setPadding(0, 0, 0, maxOf(imeHeight, navBarHeight))
                insets
            }
        }
    }
}
