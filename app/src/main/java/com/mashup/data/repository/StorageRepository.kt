package com.mashup.data.repository

import com.mashup.data.dto.StorageResponse
import com.mashup.core.network.Response
import com.mashup.network.dao.StorageDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StorageRepository @Inject constructor(
    private val storageDao: StorageDao
) {
    suspend fun getStorage(key: String): Response<StorageResponse> {
        return storageDao.getStorage(key)
    }
}
