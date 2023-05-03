package com.mashup.network.dao

import com.mashup.network.Response
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path

interface PopupDao {
    @PATCH("/api/v1/member-popups/{popupType}/last-viewed")
    suspend fun patchPopupViewed(
        @Path("popupType") popupType: String
    ): Response<Any>

    @GET("/api/v1/member-popups")
    suspend fun getMembersPopupKeyList(): Response<List<String>>
}
