package com.mashup.data.dto

import com.squareup.moshi.Json

class QrCreateResponse(
    @Json(name = "qrCodeUrl")
    val qrCodeUrl: String,
)