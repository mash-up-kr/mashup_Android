package com.mashup.network.dao

import com.mashup.data.dto.DanggnMemberRankResponse
import com.mashup.data.dto.DanggnPlatformRankResponse
import com.mashup.network.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * [swagger] https://api.dev-member.mash-up.kr/swagger-ui/index.html#/danggn-controller/getMemberRankUsingGET
 */
interface DanggnRankDao {
    // 당근 흔들기 개인별 랭킹
    @GET("api/v1/danggn/rank/member")
    suspend fun getDanggnMemberRank(
        @Query("generationNumber") generationNumber: Int, @Query("limit") limit: Int
    ): Response<DanggnMemberRankResponse>

    // 당근 흔들기 개인별 랭킹 전체
    @GET("api/v1/danggn/rank/member/all")
    suspend fun getDanggnAllMemberRank(
        @Query("generationNumber") generationNumber: Int
    ): Response<List<DanggnMemberRankResponse>>

    // 당근 흔들기 플랫폼별 랭킹
    @GET("api/v1/danggn/rank/platform")
    suspend fun getDanggnPlatformRank(
        @Query("generationNumber") generationNumber: Int
    ): Response<DanggnPlatformRankResponse>
}
