package com.mashup.ui.extensions

import android.view.View
import androidx.core.view.isVisible

fun View.visible() {
    isVisible = true
}

fun View.gone() {
    isVisible = false
}