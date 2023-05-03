package com.mashup.feature.danggn.data.repository

import com.mashup.feature.danggn.data.DanggnDao
import com.mashup.feature.danggn.data.dto.*
import com.mashup.network.Response
import javax.inject.Inject

class DanggnRepository @Inject constructor(
    private val danggnDao: DanggnDao
) {
    suspend fun getAllDanggnRank(
        generationNumber: Int
    ): Response<DanggnAllMemberRankResponse> {
        return danggnDao.getDanggnAllMemberRank(generationNumber)
    }

    suspend fun getPlatformDanggnRank(
        generationNumber: Int
    ): Response<List<DanggnPlatformRankResponse>> {
        return danggnDao.getDanggnPlatformRank(generationNumber)
    }

    suspend fun postDanggnScore(
        generationNumber: Int,
        scoreRequest: DanggnScoreRequest
    ): Response<DanggnScoreResponse> {
        return danggnDao.postDanggnScore(
            generationNumber = generationNumber,
            scoreRequest = scoreRequest
        )
    }

    suspend fun getDanggnRandomTodayMessage(): Response<DanggnRandomTodayMessageResponse> {
        return danggnDao.getDanggnRandomTodayMessage()
    }

    suspend fun getGoldDanggnPercent(): Response<GoldenDanggnPercentResponse> {
        return danggnDao.getGoldenDanggnPercent()
    }
}
