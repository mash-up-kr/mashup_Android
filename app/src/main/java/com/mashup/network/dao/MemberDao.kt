package com.mashup.network.dao

import com.mashup.common.Response
import com.mashup.data.dto.*
import com.mashup.data.model.Platform
import retrofit2.http.*

interface MemberDao {

    @POST("api/v1/members/login")
    suspend fun postLogin(
        @Body loginRequest: LoginRequest
    ): Response<AccessResponse>

    @POST("api/v1/members/token")
    suspend fun postToken(): Response<TokenResponse>

    @POST("api/v1/members/signup")
    suspend fun postSignUp(
        @Body signUpRequest: SignUpRequest
    ): Response<AccessResponse>

    @GET("api/v1/members/code")
    suspend fun getValidateSignUpCode(
        @Query("inviteCode") inviteCode: String,
        @Query("platform") platform: Platform
    ): Response<ValidResponse>

    @GET("api/v1/members/{id}")
    suspend fun getValidId(
        @Path("id") id: String
    ): Response<ValidResponse>

    @GET("api/v1/members")
    suspend fun getMember(
    ): Response<MemberInfoResponse>

    @DELETE("/api/v1/members")
    suspend fun deleteMember(): Response<Any>
}