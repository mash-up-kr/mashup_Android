package com.mashup.network.dao

import com.mashup.common.Response
import com.mashup.data.dto.AccessResponse
import com.mashup.data.dto.LoginRequest
import com.mashup.data.dto.SignUpRequest
import com.mashup.data.dto.ValidResponse
import com.mashup.data.model.Platform
import retrofit2.http.*

interface MemberDao {

    @POST("api/v1/members/login")
    suspend fun postLogin(
        @Body loginRequest: LoginRequest
    ): Response<AccessResponse>

    @POST("api/v1/members/signup")
    suspend fun postSignUp(
        @Body signUpRequest: SignUpRequest
    ): Response<AccessResponse>

    @GET("api/v1/members/code")
    suspend fun getValidateSignUpCode(
        @Query("inviteCode") inviteCode: String,
        @Query("platform") platform: Platform
    ): Response<Any>

    @GET("api/v1/members/{id}")
    suspend fun getValidId(
        @Path("id") id: String
    ): Response<ValidResponse>
}