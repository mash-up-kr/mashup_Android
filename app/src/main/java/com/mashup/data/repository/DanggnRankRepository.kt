package com.mashup.data.repository

import com.mashup.data.dto.DanggnMemberRankResponse
import com.mashup.data.dto.DanggnPlatformRankResponse
import com.mashup.network.Response
import com.mashup.network.dao.DanggnRankDao
import javax.inject.Inject

class DanggnRankRepository @Inject constructor(
    private val danggnRankDao: DanggnRankDao
) {
    suspend fun getPersonalDanggnRank(
        generationNumber: Int,
        limit: Int
    ): Response<DanggnMemberRankResponse> {
        return danggnRankDao.getDanggnMemberRank(generationNumber, limit)
    }

    suspend fun getAllDanggnRank(
        generationNumber: Int
    ): Response<List<DanggnMemberRankResponse>> {
        return danggnRankDao.getDanggnAllMemberRank(generationNumber)
    }

    suspend fun getPlatformDanggnRank(
        generationNumber: Int
    ): Response<DanggnPlatformRankResponse> {
        return danggnRankDao.getDanggnPlatformRank(generationNumber)
    }
}
