package com.mashup.common

import com.mashup.R

enum class NavigationAnimationType(
    val enterIn: Int,
    val enterOut: Int,
    val exitOut: Int
) {
    SLIDE(
        enterIn = R.anim.slide_in_right,
        enterOut = R.anim.slide_in_left,
        exitOut = R.anim.slide_out_left
    ),
    PULL(
        enterIn = R.anim.pull_in_up,
        enterOut = R.anim.pull_in_down,
        exitOut = R.anim.pull_out_down
    )
}