package com.mashup.feature.danggn.data.danggn

import javax.inject.Inject

class DanggnScoreController @Inject constructor() {

    companion object {
        private const val SCORE_REMAIN_TIME = 3000
        private const val COMBO_TERM_DURATION = 2000L
    }

    private val danggnScoreModelList = mutableListOf<DanggnScoreModel>()
    private var comboScore: Int = 0
    private var lastComboScore: Int = 0
    private var lastAddedScoreTimeMillis: Long = 0

    fun addScore(currentMode: DanggnMode) {
        danggnScoreModelList.add(
            DanggnScoreModel(
                initTimeMillis = System.currentTimeMillis(),
                mode = currentMode
            )
        )
        lastAddedScoreTimeMillis = System.currentTimeMillis()
        comboScore = currentMode.getNextScore(comboScore)
    }

    fun checkDanggnScore() {
        danggnScoreModelList.removeIf { score ->
            val timeDiff = System.currentTimeMillis() - score.initTimeMillis
            timeDiff >= SCORE_REMAIN_TIME
        }
        if (isEndOfComboTimeEnd()) {
            lastComboScore = comboScore
            comboScore = 0
        }
    }

    private fun isEndOfComboTimeEnd() =
        (System.currentTimeMillis() - lastAddedScoreTimeMillis) >= COMBO_TERM_DURATION

    fun getLastCombonScore(): Int {
        val comboScore = lastComboScore
        lastComboScore = 0
        return comboScore
    }

    fun getDanggnScoreList() = danggnScoreModelList

    fun reset() {
        comboScore = 0
        lastAddedScoreTimeMillis = 0
        danggnScoreModelList.clear()
    }
}

data class DanggnScoreModel(
    val initTimeMillis: Long,
    val mode: DanggnMode
)