package com.mashup.ui.model

data class AttendanceModel (
    val id: Int,
    val type: Int
) {
    companion object {
        val EMPTY = AttendanceModel(
            id = 0,
            type = 0
        )
    }
}