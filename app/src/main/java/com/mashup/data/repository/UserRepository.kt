package com.mashup.data.repository

import com.mashup.data.datastore.UserDataSource
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userDataSource: UserDataSource
) {
    fun getUserToken(): String? = userDataSource.token

    fun getUserGenerationNumbers(): List<Int>? = userDataSource.generateNumbers

    fun getUserMemberId(): Int? = userDataSource.memberId

    fun clearUserData() {
        userDataSource.memberId = null
        userDataSource.token = null
        userDataSource.generateNumbers = null
    }

    fun setUserToken(token: String?) {
        userDataSource.token = token
    }

    fun setUserFcmToken(fcmToken: String) {
        userDataSource.fcmToken = fcmToken
    }

    fun setUserData(
        token: String?,
        memberId: Int?,
        generationNumbers: List<Int>?
    ) {
        userDataSource.token = token
        userDataSource.memberId = memberId
        userDataSource.generateNumbers = generationNumbers
    }
}