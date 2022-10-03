package com.mashup.common.utils

import android.app.Dialog
import android.content.Context
import android.view.KeyEvent
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import com.mashup.common.R

object ProgressbarUtil {
    fun show(
        context: Context,
        cancelTouchOutside: Boolean = false
    ): Dialog {
        return createProgressDialog(context, cancelTouchOutside).apply {
            show()
        }
    }

    private fun createProgressDialog(context: Context, cancelTouchOutside: Boolean): Dialog {
        val progressBarDialog = Dialog(context, R.style.NoDimDialog)
        val progressBar = ProgressBar(context)
        val color = ContextCompat.getColor(progressBar.context, R.color.brand500)
        progressBar.indeterminateDrawable.colorFilter =
            BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
                color,
                BlendModeCompat.SRC_IN
            )
        progressBarDialog.addContentView(
            progressBar,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        )
        progressBarDialog.setCanceledOnTouchOutside(cancelTouchOutside)
        progressBarDialog.setOnKeyListener { _, keyCode, _ -> keyCode == KeyEvent.KEYCODE_BACK }
        return progressBarDialog
    }
}
