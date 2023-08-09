package com.mashup.feature.danggn.data.repository

import com.mashup.core.network.Response
import com.mashup.feature.danggn.data.DanggnDao
import com.mashup.feature.danggn.data.dto.DanggnMemberRankResponse
import com.mashup.feature.danggn.data.dto.DanggnPlatformRankResponse
import com.mashup.feature.danggn.data.dto.DanggnRandomTodayMessageResponse
import com.mashup.feature.danggn.data.dto.DanggnRankingMultipleRoundCheckResponse
import com.mashup.feature.danggn.data.dto.DanggnRankingRewardCommentRequest
import com.mashup.feature.danggn.data.dto.DanggnRankingSingleRoundCheckResponse
import com.mashup.feature.danggn.data.dto.DanggnScoreRequest
import com.mashup.feature.danggn.data.dto.DanggnScoreResponse
import com.mashup.feature.danggn.data.dto.GoldenDanggnPercentResponse
import javax.inject.Inject

class DanggnRepository @Inject constructor(
    private val danggnDao: DanggnDao
) {
    suspend fun getAllDanggnRank(
        danggnRankingRoundId: Int,
        generationNumber: Int
    ): Response<List<DanggnMemberRankResponse>> {
        return danggnDao.getDanggnAllMemberRank(
            danggnRankingRoundId = danggnRankingRoundId,
            generationNumber = generationNumber
        )
    }

    suspend fun getPlatformDanggnRank(
        danggnRankingRoundId: Int,
        generationNumber: Int
    ): Response<List<DanggnPlatformRankResponse>> {
        return danggnDao.getDanggnPlatformRank(
            danggnRankingRoundId = danggnRankingRoundId,
            generationNumber = generationNumber
        )
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

    suspend fun getDanggnMultipleRound(): Response<DanggnRankingMultipleRoundCheckResponse> {
        return danggnDao.getDanggnMultipleRound()
    }

    suspend fun getDanggnSingleRound(danggnRankingRoundId: Int): Response<DanggnRankingSingleRoundCheckResponse> {
        return danggnDao.getDanggnSingleRound(danggnRankingRoundId)
    }

    suspend fun postDanggnRankingRewardComment(danggnRankingRoundId: Int, comment: String): Response<Boolean> {
        return danggnDao.postDanggnRankingRewardComment(danggnRankingRoundId, DanggnRankingRewardCommentRequest(comment))
    }
}
