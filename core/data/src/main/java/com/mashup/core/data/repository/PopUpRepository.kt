package com.mashup.core.data.repository

import com.mashup.core.network.Response
import com.mashup.core.network.dao.PopupDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PopUpRepository @Inject constructor(
    private val popupDao: PopupDao
) {
    suspend fun patchPopupViewed(popupType: String): Response<Any> {
        return popupDao.patchPopupViewed(popupType)
    }

    suspend fun getPopupKeyList(): Response<List<String>> {
        return popupDao.getMembersPopupKeyList()
    }

    suspend fun patchPopupDisabled(popupType: String): Response<Any> {
        return popupDao.patchPopupDisabled(popupType)
    }
}
