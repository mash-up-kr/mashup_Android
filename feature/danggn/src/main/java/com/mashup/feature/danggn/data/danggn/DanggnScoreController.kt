package com.mashup.feature.danggn.data.danggn

import javax.inject.Inject

class DanggnScoreController @Inject constructor() {

    companion object {
        private const val SCORE_REMAIN_TIME = 3000
        private const val COMBO_TERM_DURATION = 2000L
    }

    private val danggnScoreList = mutableListOf<DanggnScore>()

    private var lastAddedScoreTimeMillis: Long = 0

    fun addScore(currentMode: DanggnMode) {
        danggnScoreList.add(
            DanggnScore(
                initTimeMillis = System.currentTimeMillis(),
                mode = currentMode
            )
        )
        lastAddedScoreTimeMillis = System.currentTimeMillis()
    }

    fun checkRemainDanggnScore() {
        danggnScoreList.removeIf { score ->
            val timeDiff = System.currentTimeMillis() - score.initTimeMillis
            timeDiff >= SCORE_REMAIN_TIME
        }
    }

    fun isComboTime() =
        (System.currentTimeMillis() - lastAddedScoreTimeMillis) >= COMBO_TERM_DURATION

    fun getDanggnScoreList() = danggnScoreList

    fun reset() {
        lastAddedScoreTimeMillis = 0
        danggnScoreList.clear()
    }
}

data class DanggnScore(
    val initTimeMillis: Long,
    val mode: DanggnMode
)