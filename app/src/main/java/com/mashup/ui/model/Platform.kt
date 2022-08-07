package com.mashup.ui.model

enum class Platform(val detailName: String) {
    DESIGN("Product Design"),
    ANDROID("Android"),
    IOS("iOS"),
    WEB("Web"),
    SPRING("Spring"),
    NODE("Node"),
    NONE("");

    companion object {
        fun getPlatform(platformName: String?): Platform {
            return Platform.values().find { it.name == platformName?.uppercase() } ?: NONE
        }
    }
}
