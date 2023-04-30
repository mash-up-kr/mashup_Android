package com.mashup.feature.danggn.data.danggn

sealed interface DanggnMode {
    fun getNextScore(currentScore: Int): Int
}

object NormalDanggnMode : DanggnMode {
    override fun getNextScore(currentScore: Int) = currentScore + 1

}

object GoldenDanggnMode : DanggnMode {
    override fun getNextScore(currentScore: Int) = currentScore + 100
}