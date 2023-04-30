package com.mashup.feature.danggn.data.danggn

import javax.inject.Inject
import kotlin.random.Random

class DanggnModeController @Inject constructor() {

    private var currentMode = NormalDanggnMode
    private var goldenDanggnPercent = 0

    fun getDanggnMode() = currentMode

    fun setGoldDanggnPercent(percent: Int) {
        goldenDanggnPercent = percent
    }

    fun canSwitchToGoldenDanggnMode(): Boolean {
        return Random.nextInt(1, 100) <= goldenDanggnPercent
    }

    fun reset() {
        currentMode = NormalDanggnMode
    }
}