package com.mashup.network.dao

import com.mashup.core.network.Response
import com.mashup.data.dto.ScoreHistoryResponse
import com.mashup.feature.mypage.profile.data.dto.MemberProfileResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface MemberProfileDao {
    @GET("api/v1/member-profiles/{memberId}")
    suspend fun getMemberProfile(
        @Path("memberId") memberId: String
    ): Response<MemberProfileResponse>

    @GET("api/v1/score-history/{memberId}")
    suspend fun getMemberScore(
        @Path("memberId") memberId: String
    ): Response<ScoreHistoryResponse>
}
