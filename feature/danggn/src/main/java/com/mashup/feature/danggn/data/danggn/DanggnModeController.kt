package com.mashup.feature.danggn.data.danggn

import javax.inject.Inject
import kotlin.random.Random

class DanggnModeController @Inject constructor() {

    private var currentMode: DanggnMode = NormalDanggnMode
    private var goldenDanggnPercent = 0

    private var danggnChangedTimeMillis: Long = 0

    fun getDanggnMode() = currentMode

    fun setGoldDanggnPercent(percent: Int) {
        goldenDanggnPercent = percent
    }

    fun switchToGoldenDanggnMode() {
        if (getDanggnMode() == NormalDanggnMode) return
        val changeAvailableDanggnMode = Random.nextInt(1, 100) <= goldenDanggnPercent

        if (changeAvailableDanggnMode) {
            danggnChangedTimeMillis = System.currentTimeMillis()
            currentMode = GoldenDanggnMode
        }
    }

    fun reset() {
        currentMode = NormalDanggnMode
        danggnChangedTimeMillis = 0
    }
}