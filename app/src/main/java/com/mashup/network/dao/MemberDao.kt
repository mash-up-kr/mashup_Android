package com.mashup.network.dao

import com.mashup.common.Response
import com.mashup.data.dto.SignUpRequest
import com.mashup.data.model.Platform
import retrofit2.http.Body
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Query

interface MemberDao {

    @FormUrlEncoded
    @POST("api/v1/members/signup")
    suspend fun postSignUp(
        @Body signUpRequest: SignUpRequest
    ): Response<Unit>

    @POST("api/v1/members/code")
    suspend fun getValidateSignUpCode(
        @Query("inviteCode") inviteCode: String,
        @Query("platform") platform: Platform
    ): Response<Unit>
}