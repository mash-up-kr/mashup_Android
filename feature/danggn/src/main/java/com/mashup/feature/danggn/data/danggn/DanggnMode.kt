package com.mashup.feature.danggn.data.danggn

import kotlin.random.Random

sealed interface DanggnMode {
    fun getNextScore(currentScore: Int): Int
}

object NormalDanggnMode : DanggnMode {
    override fun getNextScore(currentScore: Int) = currentScore + 1

    /**
     * @param goldenDanggnPercent 0 util 100
     */
    fun canSwitchToGoldenDanggnMode(goldenDanggnPercent: Int): Boolean {
        return Random.nextInt(1, 100 + 1) <= goldenDanggnPercent
    }
}

object GoldenDanggnMode : DanggnMode {
    override fun getNextScore(currentScore: Int) = currentScore + 100
}