package com.mashup.network.dao

import com.mashup.data.dto.SignUpRequest
import retrofit2.http.Body
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface MemberDao {

    @FormUrlEncoded
    @POST("api/v1/members/signup")
    suspend fun postSignUp(
        @Body signUpRequest: SignUpRequest
    ):
}