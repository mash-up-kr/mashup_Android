package com.mashup.feature.setting.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class SNSModel(
    @StringRes val name: Int,
    @DrawableRes val iconRes: Int,
    val link: String
)
