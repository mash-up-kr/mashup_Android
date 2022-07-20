package com.mashup.ui.mypage

import com.mashup.R

enum class AttendanceType(val title: String, val score: Double, val resourceId: Int) {
    PLACEHOLDER_HISTORY("플레이스홀더", 0.0, R.drawable.img_placeholder_history),
    PRESENTATION("전체세미나발표", 1.0, R.drawable.img_presentation),
    PROJECT_FAIL("프로젝트 배포 실패", -0.5, R.drawable.img_projectfail),
    PROJECT_SUCCESS("프로젝트 배포 성공", 0.5, R.drawable.img_projectsuccess),
    ATTENDANCE("출석", 1.0, R.drawable.img_attendance),
    ABSENT("결석", -1.0, R.drawable.img_absent),
    LATE("지각", -0.5, R.drawable.img_late),
    HACKATHON_PREPARE("해커톤준비위원회", 1.0, R.drawable.img_hackathonprepare),
    PROJECT_LEADER("프로젝트 팀장", 2.0, R.drawable.img_projectleader),
    PROJECT_SUB_LEADER("프로젝트 부팀장", 2.0, R.drawable.img_projectsubleader),
    STAFF("스태프", 99.0, R.drawable.img_staff),
    MASHUP_LEADER("회장", 100.0, R.drawable.img_mashupleader),
    MASHUP_SUB_LEADER("부회장", 100.0, R.drawable.img_mashupsubleader),
    TECH_BLOG_WRITE("기술블로그 작성", 1.0, R.drawable.img_techblogwrite),
    MASHUP_CONTENTS_WRITE("Mash-Up 콘텐츠 글 작성", 0.5, R.drawable.img_mashupcontentswrite),
}