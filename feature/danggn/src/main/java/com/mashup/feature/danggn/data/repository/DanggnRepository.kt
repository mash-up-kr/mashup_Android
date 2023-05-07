package com.mashup.feature.danggn.data.repository

import com.mashup.feature.danggn.data.DanggnDao
import com.mashup.feature.danggn.data.dto.DanggnAllMemberRankResponse
import com.mashup.feature.danggn.data.dto.DanggnPlatformRankResponse
import com.mashup.feature.danggn.data.dto.DanggnRandomTodayMessageResponse
import com.mashup.feature.danggn.data.dto.DanggnScoreRequest
import com.mashup.feature.danggn.data.dto.DanggnScoreResponse
import com.mashup.feature.danggn.data.dto.GoldenDanggnPercentResponse
import com.mashup.network.Response2
import javax.inject.Inject

class DanggnRepository @Inject constructor(
    private val danggnDao: DanggnDao
) {
    suspend fun getAllDanggnRank(
        generationNumber: Int
    ): Response2<DanggnAllMemberRankResponse> {
        return danggnDao.getDanggnAllMemberRank(generationNumber)
    }

    suspend fun getPlatformDanggnRank(
        generationNumber: Int
    ): Response2<List<DanggnPlatformRankResponse>> {
        return danggnDao.getDanggnPlatformRank(generationNumber)
    }

    suspend fun postDanggnScore(
        generationNumber: Int,
        scoreRequest: DanggnScoreRequest
    ): Response2<DanggnScoreResponse> {
        return danggnDao.postDanggnScore(
            generationNumber = generationNumber,
            scoreRequest = scoreRequest
        )
    }

    suspend fun getDanggnRandomTodayMessage(): Response2<DanggnRandomTodayMessageResponse> {
        return danggnDao.getDanggnRandomTodayMessage()
    }

    suspend fun getGoldDanggnPercent(): Response2<GoldenDanggnPercentResponse> {
        return danggnDao.getGoldenDanggnPercent()
    }
}
