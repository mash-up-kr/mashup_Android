package com.mashup.common.extensions

import android.graphics.Paint
import android.widget.TextView

fun TextView.setUnderLine() {
    paintFlags = paintFlags or Paint.UNDERLINE_TEXT_FLAG
}