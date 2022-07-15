package com.mashup.data.model

enum class Platform {
    ANDROID, DESIGN, IOS, NODE, SPRING, WEB, UNKNOWN;

    companion object {
        fun getPlatform(platformName: String): Platform {
            return values().find { it.name == platformName.uppercase() } ?: UNKNOWN
        }
    }
}