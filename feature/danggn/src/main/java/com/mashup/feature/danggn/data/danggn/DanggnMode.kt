package com.mashup.feature.danggn.data.danggn

sealed interface DanggnMode {
    fun getNextScore(currentScore: Int): Int
}

object NormalDanggnMode : DanggnMode {
    override fun getNextScore(currentScore: Int) = currentScore + 1
}

data class GoldenDanggnMode(
    val remainTimeInSeconds: Long = 0L
) : DanggnMode {
    override fun getNextScore(currentScore: Int) = currentScore + 100
}