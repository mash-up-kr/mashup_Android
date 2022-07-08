package com.mashup.data.dto

import com.squareup.moshi.Json

class QrCodeResponse(
    @Json(name = "qrCodeUrl")
    val qrCodeUrl: String,
)