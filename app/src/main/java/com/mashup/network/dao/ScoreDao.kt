package com.mashup.network.dao

import com.mashup.data.dto.ScoreHistoryResponseList
import com.mashup.network.Response
import retrofit2.http.GET

interface ScoreDao {
    @GET("api/v1/score-history")
    suspend fun getScoreHistory(): Response<ScoreHistoryResponseList>
}