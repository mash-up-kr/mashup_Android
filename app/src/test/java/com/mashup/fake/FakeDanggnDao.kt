package com.mashup.fake

import com.mashup.feature.danggn.data.DanggnDao
import com.mashup.feature.danggn.data.dto.DanggnMemberRankResponse
import com.mashup.feature.danggn.data.dto.DanggnPlatformRankResponse
import com.mashup.feature.danggn.data.dto.DanggnRandomTodayMessageResponse
import com.mashup.feature.danggn.data.dto.DanggnRankingMultipleRoundCheckResponse
import com.mashup.feature.danggn.data.dto.DanggnRankingSingleRoundCheckResponse
import com.mashup.feature.danggn.data.dto.DanggnScoreRequest
import com.mashup.feature.danggn.data.dto.DanggnScoreResponse
import com.mashup.feature.danggn.data.dto.GoldenDanggnPercentResponse
import com.mashup.core.network.Response

class FakeDanggnDao : DanggnDao {

    override suspend fun getDanggnAllMemberRank(
        danggnRankingRoundId: Int,
        generationNumber: Int,
        limit: Int
    ): Response<List<DanggnMemberRankResponse>> {
        return Response(
            code = "SUCCESS",
            message = "",
            data = listOf(
                DanggnMemberRankResponse(
                    39, "정종노드", 150
                ),
                DanggnMemberRankResponse(
                    40, "정종드투", 151
                ),
                DanggnMemberRankResponse(
                    41, "정종민", 152
                ),
                DanggnMemberRankResponse(
                    42, "정종웹", 153
                ),
                DanggnMemberRankResponse(
                    43, "정종오스", 154
                ),
                DanggnMemberRankResponse(
                    44, "정종자인", 155
                ),
            ),
            page = null,
        )
    }

    override suspend fun getDanggnPlatformRank(
        danggnRankingRoundId: Int,
        generationNumber: Int
    ): Response<List<DanggnPlatformRankResponse>> {
        return Response("", null, null, null)
    }

    override suspend fun postDanggnScore(
        generationNumber: Int,
        scoreRequest: DanggnScoreRequest
    ): Response<DanggnScoreResponse> {
        return Response(
            "null", null, null, null
        )
    }

    override suspend fun getDanggnRandomTodayMessage(): Response<DanggnRandomTodayMessageResponse> {
        return Response(
            "null", null, null, null
        )
    }

    override suspend fun getGoldenDanggnPercent(): Response<GoldenDanggnPercentResponse> {
        return Response(
            "null", null, null, null
        )
    }

    override suspend fun getDanggnMultipleRound(): Response<DanggnRankingMultipleRoundCheckResponse> {
        return Response(
            "SUCCESS", null, DanggnRankingMultipleRoundCheckResponse(
                listOf(
                    DanggnRankingMultipleRoundCheckResponse.DanggnRankingRound(
                        id = 2,
                        number = 2,
                        startDate = "2023-06-17",
                        endDate = "2023-06-20"
                    ),
                    DanggnRankingMultipleRoundCheckResponse.DanggnRankingRound(
                        id = 1,
                        number = 1,
                        startDate = "2023-06-18",
                        endDate = "2023-06-18"
                    ),
                )
            ), null
        )
    }

    override suspend fun getDanggnSingleRound(danggnRankingRoundId: Int): Response<DanggnRankingSingleRoundCheckResponse> {
        return Response(
            "null", null, null, null
        )
    }
}