package com.mashup.data.datastore

data class User(
    val token: String,
    val name: String,
    val memberId: Int,
    val generationNumber: Int,
)
