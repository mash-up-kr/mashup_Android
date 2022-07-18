package com.mashup.network.dao

import com.mashup.common.Response
import com.mashup.data.dto.LoginRequest
import com.mashup.data.dto.TokenResponse
import com.mashup.data.dto.SignUpRequest
import com.mashup.data.model.Platform
import retrofit2.http.*

interface MemberDao {

    @POST("api/v1/members/login")
    suspend fun postLogin(
        @Body loginRequest: LoginRequest
    ): Response<TokenResponse>

    @POST("api/v1/members/signup")
    suspend fun postSignUp(
        @Body signUpRequest: SignUpRequest
    ): Response<TokenResponse>

    @GET("api/v1/members/code")
    suspend fun getValidateSignUpCode(
        @Query("inviteCode") inviteCode: String,
        @Query("platform") platform: Platform
    ): Response<Any>
}