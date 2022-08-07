package com.mashup.ui.mypage

import androidx.annotation.DrawableRes
import com.mashup.R

enum class AttendanceType(val title: String, val score: Double, @DrawableRes val resourceId: Int) {
    ETC("플레이스홀더", 0.0, R.drawable.img_placeholder_history),
    SEMINAR_PRESENTATION("전체세미나발표", 1.0, R.drawable.img_presentation),
    DEPLOY_FAILURE("프로젝트 배포 실패", -0.5, R.drawable.img_projectfail),
    DEPLOY_SUCCESS("프로젝트 배포 성공", 0.5, R.drawable.img_projectsuccess),
    ATTENDANCE("출석", 1.0, R.drawable.img_attendance),
    ABSENT("결석", -1.0, R.drawable.img_absent),
    LATE("지각", -0.5, R.drawable.img_late),
    HACKATHON_COMMITTEE("해커톤준비위원회", 1.0, R.drawable.img_hackathonprepare),
    PROJECT_LEADER("프로젝트 팀장", 2.0, R.drawable.img_projectleader),
    PROJECT_SUBLEADER("프로젝트 부팀장", 2.0, R.drawable.img_projectsubleader),
    MASHUP_STAFF("스태프", 99.0, R.drawable.img_staff),
    MASHUP_LEADER("회장", 100.0, R.drawable.img_mashupleader),
    MASHUP_SUBLEADER("부회장", 100.0, R.drawable.img_mashupsubleader),
    TECH_BLOG_WRITE("기술블로그 작성", 1.0, R.drawable.img_techblogwrite),
    MASHUP_CONTENTS_WRITE("Mash-Up 콘텐츠 글 작성", 0.5, R.drawable.img_mashupcontentswrite);

    companion object {
        fun getAttendanceType(scoreType: String?): AttendanceType {
            return values().find { it.name == scoreType?.uppercase() }
                ?: ETC
        }
    }
}