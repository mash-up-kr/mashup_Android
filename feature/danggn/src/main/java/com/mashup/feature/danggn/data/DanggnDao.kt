package com.mashup.feature.danggn.data

import com.mashup.feature.danggn.data.dto.DanggnAllMemberRankResponse
import com.mashup.feature.danggn.data.dto.DanggnMemberRankResponse
import com.mashup.feature.danggn.data.dto.DanggnPlatformRankResponse
import com.mashup.feature.danggn.data.dto.DanggnScoreRequest
import com.mashup.feature.danggn.data.dto.DanggnScoreResponse
import com.mashup.network.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * [swagger] https://api.dev-member.mash-up.kr/swagger-ui/index.html#/danggn-controller/getMemberRankUsingGET
 */
interface DanggnDao {
    // 당근 흔들기 개인별 랭킹
    @Deprecated("동시성 문제로 사용하지 않는 API 입니다.")
    @GET("api/v1/danggn/rank/member")
    suspend fun getDanggnMemberRank(
        @Query("generationNumber") generationNumber: Int, @Query("limit") limit: Int
    ): Response<DanggnMemberRankResponse>

    // 당근 흔들기 개인별 랭킹 전체
    @GET("api/v1/danggn/rank/member/all")
    suspend fun getDanggnAllMemberRank(
        @Query("generationNumber") generationNumber: Int
    ): Response<DanggnAllMemberRankResponse>

    // 당근 흔들기 플랫폼별 랭킹
    @GET("api/v1/danggn/rank/platform")
    suspend fun getDanggnPlatformRank(
        @Query("generationNumber") generationNumber: Int
    ): Response<List<DanggnPlatformRankResponse>>

    // 당근 흔들기 플랫폼별 랭킹
    @POST("api/v1/danggn/score")
    suspend fun postDanggnScore(
        @Query("generationNumber") generationNumber: Int,
        @Body scoreRequest: DanggnScoreRequest
    ): Response<DanggnScoreResponse>
}
