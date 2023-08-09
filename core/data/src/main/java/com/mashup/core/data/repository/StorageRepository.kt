package com.mashup.core.data.repository

import com.mashup.core.network.Response
import com.mashup.core.network.dao.StorageDao
import com.mashup.core.network.dto.StorageResponse
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
