package com.mashup.network.dao

import com.mashup.core.model.Platform
import com.mashup.core.network.Response
import com.mashup.data.dto.AccessResponse
import com.mashup.data.dto.LoginRequest
import com.mashup.data.dto.MemberInfoResponse
import com.mashup.data.dto.PushNotificationRequest
import com.mashup.data.dto.SignUpRequest
import com.mashup.data.dto.TokenResponse
import com.mashup.data.dto.ValidResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

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
    suspend fun getMember(): Response<MemberInfoResponse>

    @DELETE("/api/v1/members")
    suspend fun deleteMember(): Response<Any>

    @PATCH("/api/v1/members/push-notification")
    suspend fun patchPushNotification(
        @Body pushNotificationRequest: PushNotificationRequest
    ): Response<Boolean>
}
