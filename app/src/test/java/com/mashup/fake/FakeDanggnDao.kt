package com.mashup.fake

import com.mashup.feature.danggn.data.DanggnDao
import com.mashup.feature.danggn.data.dto.DanggnAllMemberRankResponse
import com.mashup.feature.danggn.data.dto.DanggnMemberRankResponse
import com.mashup.feature.danggn.data.dto.DanggnPlatformRankResponse
import com.mashup.feature.danggn.data.dto.DanggnScoreRequest
import com.mashup.feature.danggn.data.dto.DanggnScoreResponse
import com.mashup.network.Response

class FakeDanggnDao : DanggnDao {
    override suspend fun getDanggnMemberRank(
        generationNumber: Int,
        limit: Int
    ): Response<DanggnMemberRankResponse> {
        return Response(
            code = "",
            message = null,
            data = null,
            page = null
        )
    }

    override suspend fun getDanggnAllMemberRank(generationNumber: Int): Response<DanggnAllMemberRankResponse> {
        return Response(
            code = "SUCCESS",
            message = "",
            data = DanggnAllMemberRankResponse(
                listOf(
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
                limit = 11
            ),
            page = null,
        )
    }

    override suspend fun getDanggnPlatformRank(generationNumber: Int): Response<List<DanggnPlatformRankResponse>> {
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
}