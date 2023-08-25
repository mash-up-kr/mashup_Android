package com.mashup.feature.mypage.profile.data

import com.mashup.feature.mypage.profile.data.dto.MemberGenerationRequest
import com.mashup.feature.mypage.profile.data.dto.MemberProfileRequest
import com.mashup.feature.mypage.profile.edit.EditedProfile
import javax.inject.Inject

class MyProfileRepository @Inject constructor(
    private val myProfileDao: MyProfileDao,
    private val memberGenerationDao: MemberGenerationDao,
) {
    suspend fun getMemberGenerations() = memberGenerationDao.getMemberGeneration()

    suspend fun postMemberGenerations(id: Long, projectTeamName: String, role: String) =
        memberGenerationDao.postMemberGeneration(
            id = id, MemberGenerationRequest(
                projectTeamName = projectTeamName,
                role = role
            )
        )

    suspend fun getMyProfile() = myProfileDao.getMemberProfile()

    suspend fun postMyProfile(
        editedProfile: EditedProfile
    ) = myProfileDao.postMemberProfile(
        MemberProfileRequest(
            birthDate = editedProfile.birthDay, // 생년월일
            blogLink = editedProfile.tistory, // 티스토리
            company = editedProfile.company, // 회사명
            githubLink = editedProfile.github, //  깃헙
            introduction = editedProfile.introduceMySelf, // 자기소개
            job = editedProfile.work, // 직군
            linkedInLink = editedProfile.linkedIn, // 링크드인
            portfolioLink = editedProfile.behance, // 포트폴리오
            residence = editedProfile.location, // 출몰지역
            socialNetworkServiceLink = editedProfile.instagram, // sns
        )
    )
}