package com.mashup.core.common.utils

import java.text.DecimalFormat

fun thousandFormat(number: Int): String = DecimalFormat("#,###").format(number.toLong())
