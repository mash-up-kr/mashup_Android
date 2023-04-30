package com.mashup.feature.danggn.data.danggn

import javax.inject.Inject
import kotlin.random.Random

class DanggnModeController @Inject constructor() {

    companion object {
        private const val GOLDEN_MODE_TIME = 3000
    }

    private var currentMode: DanggnMode = NormalDanggnMode
    private var goldenDanggnPercent = 0

    private var danggnChangedTimeMillis: Long = 0

    fun getDanggnMode() = currentMode

    fun setGoldDanggnPercent(percent: Int) {
        goldenDanggnPercent = percent
    }

    fun switchToGoldenDanggnMode() {
        if (getDanggnMode() == GoldenDanggnMode) return
        val changeAvailableDanggnMode = Random.nextInt(1, 100) <= goldenDanggnPercent

        if (changeAvailableDanggnMode) {
            danggnChangedTimeMillis = System.currentTimeMillis()
            currentMode = GoldenDanggnMode
        }
    }

    private fun switchToNormalDanggnMode() {
        if (getDanggnMode() == NormalDanggnMode) return

        val timeDiff = System.currentTimeMillis() - danggnChangedTimeMillis
        if (timeDiff >= GOLDEN_MODE_TIME) {
            danggnChangedTimeMillis = 0
            currentMode = NormalDanggnMode
        }
    }

    fun checkDanggnMode() {
        if (getDanggnMode() == GoldenDanggnMode) {
            switchToNormalDanggnMode()
        }
    }


    fun reset() {
        currentMode = NormalDanggnMode
        danggnChangedTimeMillis = 0
    }
}