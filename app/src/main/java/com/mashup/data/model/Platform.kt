package com.mashup.data.model

enum class Platform {
    ANDROID, DESIGN, IOS, NODE, SPRING, WEB, UNKNOWN;

    fun getName(): String {
        return when (this) {
            ANDROID -> "Android"
            DESIGN -> "Product Design"
            IOS -> "iOS"
            NODE -> "Node"
            SPRING -> "Spring"
            WEB -> "Web"
            else -> "Unknown"
        }
    }

    companion object {
        fun getPlatform(platformName: String): Platform {
            return values().find { it.name == platformName.uppercase() } ?: UNKNOWN
        }
    }
}