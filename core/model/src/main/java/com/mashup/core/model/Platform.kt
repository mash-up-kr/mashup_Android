package com.mashup.core.model

enum class Platform(val detailName: String) {
    DESIGN("Product Design"),
    ANDROID("Android"),
    IOS("iOS"),
    WEB("Web"),
    SPRING("Spring"),
    NODE("Node"),
    UNKNOWN("Unknown");

    companion object {
        fun getPlatform(platformName: String?): Platform {
            return values().find { it.name == platformName?.uppercase() } ?: UNKNOWN
        }
    }
}
