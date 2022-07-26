package com.mashup.widget

import android.content.Context
import android.view.Gravity
import android.widget.Toast

class MashUpToast(context: Context) : Toast(context) {

    init {
        setGravity(Gravity.TOP or Gravity.CENTER, 0, 0)
        duration = LENGTH_LONG
    }
}