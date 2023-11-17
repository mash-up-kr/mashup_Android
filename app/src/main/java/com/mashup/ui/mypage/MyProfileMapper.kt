package com.mashup.ui.mypage

import com.mashup.data.model.ScoreDetails
import com.mashup.feature.mypage.profile.data.dto.MemberGenerationsResponse
import com.mashup.feature.mypage.profile.data.dto.MemberProfileResponse
import com.mashup.feature.mypage.profile.model.ProfileCardData
import com.mashup.feature.mypage.profile.model.ProfileData
import com.mashup.ui.model.ActivityHistory
import javax.inject.Inject

class MyProfileMapper @Inject constructor() {

    fun mapToActivityHistory(response: ScoreDetails) = ActivityHistory(
        scoreName = response.scoreName,
        attendanceType = AttendanceType.getAttendanceType(response.scoreType),
        cumulativeScore = response.cumulativeScore,
        score = response.score,
        detail = response.scheduleName,
        date = response.date
    )

    fun mapToProfileData(response: MemberProfileResponse) = ProfileData(
        birthDay = response.birthDate.orEmpty(),
        work = response.job.orEmpty(),
        company = response.company.orEmpty(),
        introduceMySelf = response.introduction.orEmpty(),
        location = response.residence.orEmpty(),
        instagram = response.socialNetworkServiceLink.orEmpty(),
        github = response.githubLink.orEmpty(),
        behance = response.portfolioLink.orEmpty(),
        linkedIn = response.linkedInLink.orEmpty(),
        tistory = response.blogLink.orEmpty()
    )
    fun mapToProfileCardData(
        name: String,
        response: MemberGenerationsResponse.MemberGeneration
    ) = ProfileCardData(
        id = response.id,
        name = name,
        isRunning = response.status != "DONE",
        generationNumber = response.number,
        platform = response.platform,
        projectTeamName = response.projectTeamName.orEmpty(),
        role = response.role.orEmpty()
    )
}
