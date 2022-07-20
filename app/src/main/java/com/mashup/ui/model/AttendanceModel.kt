package com.mashup.ui.model


data class AttendanceModel(
    val id: Int,
    val type: Int,
    val profile: Profile?,
    val generationNum: Int?
) {
    companion object {
        val EMPTY = AttendanceModel(
            id = 0,
            type = 0,
            profile = Profile.EMPTY,
            generationNum = 12,
        )
    }

    fun getGeneration() = "${generationNum}기"

}

data class Profile(
    val platform: Platform,
    val name: String,
    val score: Int,
) {
    companion object {
        val EMPTY = Profile(
            platform = Platform.NODE,
            name = "test",
            score = 2
        )
    }

    fun getAttendanceScore() = "${score}점"
}