package com.mashup.network.repository

import com.mashup.common.Response
import com.mashup.data.dto.SignUpRequest
import com.mashup.data.model.Platform
import com.mashup.network.dao.MemberDao
import javax.inject.Inject

class MemberRepository @Inject constructor(
    private val memberDao: MemberDao
) {
    suspend fun signup(
        identification: String,
        inviteCode: String,
        name: String,
        password: String,
        platform: String,
        privatePolicyAgreed: Boolean
    ): Response<Unit> {
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
    ): Response<Unit> {
        return memberDao.getValidateSignUpCode(
            inviteCode = inviteCode,
            platform = Platform.getPlatform(platform)
        )
    }
}