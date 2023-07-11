package com.mashup.service

enum class PushLinkType {
    MAIN,       // 기본 페이지
    QR,         // QR 페이지
    DANGGN,     // 당근 페이지
    DNAGGN_REWARD,
    MYPAGE,
    UNKNOWN,
    ;

    companion object {
        fun getPushLinkType(type: String) =
            values().find { it.name == type } ?: UNKNOWN
    }
}