package com.mashup.feature.danggn.data.danggn

import javax.inject.Inject

class DanggnScoreController @Inject constructor() {

    private val danggnScoreList = mutableListOf<DanggnScore>()

    fun addScore(mode: DanggnMode) {
        danggnScoreList.add(
            DanggnScore(
                initTimeMillis = System.currentTimeMillis(),
                mode = mode
            )
        )
    }

    fun checkRemainDanggnScore() {
        danggnScoreList.removeIf { score ->
            val timeDiff = System.currentTimeMillis() - score.initTimeMillis
            val timeDiffOfDay = timeDiff / 1000

            timeDiffOfDay >= SCORE_REMAIN_TIME
        }
    }

    fun reset() {
        danggnScoreList.clear()
    }

    companion object {
        private const val SCORE_REMAIN_TIME = 3000
    }
}

data class DanggnScore(
    val initTimeMillis: Long,
    val mode: DanggnMode
)