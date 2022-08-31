package com.mashup.common.extensions

import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

fun View.onThrottleFirstClick(
    viewLifecycleScope: CoroutineScope,
    duration: Long = 500L,
    clickListener: () -> Unit
) = callbackFlow {
    setOnClickListener {
        it.isEnabled = false
        trySend(it)
    }
    awaitClose { setOnClickListener(null) }
}.onEach { view ->
    clickListener()
    delay(duration)
    view.isEnabled = true
}.launchIn(viewLifecycleScope)

fun View.findYPositionInView(targetView: View, yCumulative: Int): Int {
    if (this === targetView) {
        return yCumulative
    }
    if (this is ViewGroup) {
        val parentView = this
        for (i in 0 until parentView.childCount) {
            val child = parentView.getChildAt(i)
            val yChild = yCumulative + child.y.toInt()
            val yNested = child.findYPositionInView(targetView, yChild)
            if (yNested != -1) return yNested
        }
    }
    return -1 // not found
}

fun View.visible() {
    isVisible = true
}

fun View.gone() {
    isVisible = false
}