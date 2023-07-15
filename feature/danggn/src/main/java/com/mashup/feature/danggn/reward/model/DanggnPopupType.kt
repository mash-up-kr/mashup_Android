package com.mashup.feature.danggn.reward.model

enum class DanggnPopupType {
    DANGGN_REWARD, UNKNOWN;

    companion object {
        fun getDanggnPopupType(type: String): DanggnPopupType {
            return DanggnPopupType.values().find { it.name == type } ?: UNKNOWN
        }
    }
}
