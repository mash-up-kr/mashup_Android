package com.mashup.core.common.utils

infix fun <A, B, C> Pair<A, B>.trip(that: C) = Triple(first, second, that)
