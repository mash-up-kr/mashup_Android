package com.mashup.core.network.dao

import com.mashup.core.network.Response
import com.mashup.core.network.dto.StorageResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface StorageDao {
    @GET("/api/v1/storage/key/{key}")
    suspend fun getStorage(
        @Path("key") key: String
    ): Response<StorageResponse>
}
