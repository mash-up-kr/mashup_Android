package com.mashup.data.repository

import com.mashup.common.Response
import com.mashup.data.dto.LoginRequest
import com.mashup.data.dto.SignUpRequest
import com.mashup.data.dto.TokenResponse
import com.mashup.data.model.Platform
import com.mashup.network.dao.MemberDao
import javax.inject.Inject

class MemberRepository @Inject constructor(
    private val memberDao: MemberDao
) {
    suspend fun login(
        identification: String,
        password: String
    ): Response<TokenResponse> {
        return memberDao.postLogin(
            LoginRequest(
                identification = identification,
                password = password
            )
        )
    }

    suspend fun signup(
        identification: String,
        inviteCode: String,
        name: String,
        password: String,
        platform: String,
        privatePolicyAgreed: Boolean
    ): Response<TokenResponse> {
        return memberDao.postSignUp(
            SignUpRequest(
                identification = identification,
                inviteCode = inviteCode,
                name = name,
                password = password,
                platform = Platform.getPlatform(platform),
                privatePolicyAgreed = privatePolicyAgreed
            )
        )
    }

    suspend fun validateSignUpCode(
        inviteCode: String,
        platform: String
    ): Response<Any> {
        return memberDao.getValidateSignUpCode(
            inviteCode = inviteCode,
            platform = Platform.getPlatform(platform)
        )
    }
}