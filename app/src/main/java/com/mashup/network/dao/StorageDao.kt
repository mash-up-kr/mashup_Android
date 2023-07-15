package com.mashup.network.dao

import com.mashup.data.dto.StorageResponse
import com.mashup.core.network.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface StorageDao {
    @GET("/api/v1/storage/key/{key}")
    suspend fun getStorage(
        @Path("key") key: String
    ): Response<StorageResponse>
}
