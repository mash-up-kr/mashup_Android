package com.mashup.data.repository

import com.mashup.common.Response
import com.mashup.data.dto.ScoreHistoryResponse
import com.mashup.network.dao.ScoreDao
import javax.inject.Inject

class MyPageRepository @Inject constructor(
    private val scoreDao: ScoreDao
) {
    suspend fun getScoreHistory(): Response<List<ScoreHistoryResponse>> {
        return scoreDao.getScoreHistory()
    }
}