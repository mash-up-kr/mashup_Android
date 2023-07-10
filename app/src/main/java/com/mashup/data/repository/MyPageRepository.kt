package com.mashup.data.repository

import com.mashup.data.dto.ScoreHistoryResponseList
import com.mashup.core.network.Response
import com.mashup.network.dao.ScoreDao
import javax.inject.Inject

class MyPageRepository @Inject constructor(
    private val scoreDao: ScoreDao
) {
    suspend fun getScoreHistory(): Response<ScoreHistoryResponseList> {
        return scoreDao.getScoreHistory()
    }
}
