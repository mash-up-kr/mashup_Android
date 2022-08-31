package com.mashup.common.extensions

import android.view.View
import android.widget.ScrollView

fun ScrollView.scrollToTarget(rootView: View, target: View, offset: Int = 0) {
    this.smoothScrollTo(0, rootView.findYPositionInView(target, offset))
}