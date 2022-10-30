package com.mashup.core.common.extensions

import android.graphics.Paint
import android.widget.TextView

fun TextView.setUnderLine() {
    paintFlags = paintFlags or Paint.UNDERLINE_TEXT_FLAG
}

fun TextView.addCancelLine() {
    paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
}

fun TextView.removeCancelLine() {
    paintFlags = paintFlags xor Paint.STRIKE_THRU_TEXT_FLAG
}
