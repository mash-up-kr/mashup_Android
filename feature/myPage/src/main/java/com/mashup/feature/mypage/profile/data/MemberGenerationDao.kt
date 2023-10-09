package com.mashup.feature.mypage.profile.data

import com.mashup.core.network.Response
import com.mashup.feature.mypage.profile.data.dto.MemberGenerationRequest
import com.mashup.feature.mypage.profile.data.dto.MemberGenerationsResponse
import com.mashup.feature.mypage.profile.data.dto.ValidResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path

interface MemberGenerationDao {
    @GET("api/v1/member-generations/my")
    suspend fun getMemberGeneration(): Response<MemberGenerationsResponse>

    @PATCH("api/v1/member-generations/my/{id}")
    suspend fun postMemberGeneration(
        @Path("id") id: Long,
        @Body body: MemberGenerationRequest
    ): Response<ValidResponse>
}
