package com.mashup.common.utils

import android.content.Context
import android.content.res.Resources
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.mashup.common.R
import com.mashup.common.databinding.LayoutToastBinding

object ToastUtil {
    fun showToast(context: Context, message: String) {
        val inflater = LayoutInflater.from(context)
        val binding: LayoutToastBinding =
            DataBindingUtil.inflate(inflater, R.layout.layout_toast, null, false)

        binding.tvSample.text = message
        Toast(context).apply {
            setGravity(Gravity.TOP or Gravity.CENTER, 0, 16.toPx())
            duration = Toast.LENGTH_LONG
            view = binding.root
        }.show()
    }

    private fun Int.toPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()
}
