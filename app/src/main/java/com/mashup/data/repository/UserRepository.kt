package com.mashup.data.repository

import com.mashup.data.datastore.UserDataSource
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userDataSource: UserDataSource
) {
    fun clearUserData() {
        userDataSource.memberId = null
        userDataSource.token = null
        userDataSource.generateNumbers = null
    }
}