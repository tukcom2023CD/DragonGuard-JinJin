package com.dragonguard.android.model

/*
 메인화면의 사용자의 프로필, 랭킹, 기여도, 티어등을
 받기위해 정의한 model
 */
data class UserInfoModel(
    val authStep: String?,
    val commits: Int?,
    val githubId: String?,
    val id: String?,
    val name: String?,
    val profileImage: String?,
    val tier: String?,
    val rank: String?,
    val tokenAmount: Int?,
    val organization: String?
)