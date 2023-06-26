package com.mashup.feature.danggn.data

import com.mashup.feature.danggn.data.dto.DanggnAllMemberRankResponse
import com.mashup.feature.danggn.data.dto.DanggnPlatformRankResponse
import com.mashup.feature.danggn.data.dto.DanggnRandomTodayMessageResponse
import com.mashup.feature.danggn.data.dto.DanggnRankingMultipleRoundCheckResponse
import com.mashup.feature.danggn.data.dto.DanggnRankingSingleRoundCheckResponse
import com.mashup.feature.danggn.data.dto.DanggnScoreRequest
import com.mashup.feature.danggn.data.dto.DanggnScoreResponse
import com.mashup.feature.danggn.data.dto.GoldenDanggnPercentResponse
import com.mashup.network.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * [swagger] https://api.dev-member.mash-up.kr/swagger-ui/index.html#/danggn-controller
 */
interface DanggnDao {

    // 당근 흔들기 개인별 랭킹 전체
    @GET("api/v1/danggn/rank/member/all")
    suspend fun getDanggnAllMemberRank(
        @Query("danggnRankingRoundId") danggnRankingRoundId: Int,
        @Query("generationNumber") generationNumber: Int,
    ): Response<DanggnAllMemberRankResponse>

    // 당근 흔들기 플랫폼별 랭킹
    @GET("api/v1/danggn/rank/platform")
    suspend fun getDanggnPlatformRank(
        @Query("danggnRankingRoundId") danggnRankingRoundId: Int,
        @Query("generationNumber") generationNumber: Int,
    ): Response<List<DanggnPlatformRankResponse>>

    // 당근 흔들기 플랫폼별 랭킹
    @POST("api/v1/danggn/score")
    suspend fun postDanggnScore(
        @Query("generationNumber") generationNumber: Int,
        @Body scoreRequest: DanggnScoreRequest
    ): Response<DanggnScoreResponse>

    @GET("/api/v1/danggn/random-today-message")
    suspend fun getDanggnRandomTodayMessage(): Response<DanggnRandomTodayMessageResponse>

    // 황금 당근 확률
    @GET("api/v1/danggn/golden-danggn-percent")
    suspend fun getGoldenDanggnPercent(): Response<GoldenDanggnPercentResponse>

    // 당근 랭킹 회차 단건 조회
    @GET("api/v1/danggn/ranking-round")
    suspend fun getDanggnMultipleRound(): Response<DanggnRankingMultipleRoundCheckResponse>

    // 당근 랭킹 회차 다건 조회
    @GET("api/v1/danggn/ranking-round/{danggnRankingRoundId}")
    suspend fun getDanggnSingleRound(
        @Path("danggnRankingRoundId") danggnRankingRoundId: Int
    ): Response<DanggnRankingSingleRoundCheckResponse>
}
