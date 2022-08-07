package com.mashup.data.dto

enum class SchedulesProgress {
    // 등록된 세미나가 없는 경우
    NOT_REGISTERED,

    //세미나가 존재하지만, 현재 날짜 이후로 등록된 세미나가 있는 경우
    ON_GOING,

    // 세미나가 존재하지만, 현재 날짜 이후로 등록된 세미나가 없는 경우
    DONE
}