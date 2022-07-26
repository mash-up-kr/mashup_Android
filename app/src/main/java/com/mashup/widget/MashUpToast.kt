package com.mashup.widget

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.Toast
import com.mashup.databinding.ViewMashupToastBinding
import com.mashup.extensions.dp

class MashUpToast(context: Context) : Toast(context) {

    private val viewBinding by lazy {
        ViewMashupToastBinding.inflate(
            LayoutInflater.from(context), null, false
        )
    }

    init {
        setGravity(Gravity.TOP or Gravity.CENTER, 0, -(64.dp(context)))
        duration = LENGTH_LONG
        view
    }
}