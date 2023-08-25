package com.mashup.feature.mypage.profile.data

import com.mashup.core.network.Response
import com.mashup.feature.mypage.profile.data.dto.MemberProfileRequest
import com.mashup.feature.mypage.profile.data.dto.MemberProfileResponse
import com.mashup.feature.mypage.profile.data.dto.ValidResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface MyProfileDao {
    @GET("api/v1/member-profiles/my")
    suspend fun getMemberProfile(
    ): Response<MemberProfileResponse>

    @POST("api/v1/member-profiles/my")
    suspend fun postMemberProfile(
        @Body body: MemberProfileRequest
    ): Response<ValidResponse>
}