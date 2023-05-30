package com.dragonguard.android.model

/*
 메인화면의 사용자의 프로필, 랭킹, 기여도, 티어등을
 받기위해 정의한 model
 */
data class UserInfoModel(
    var authStep: String?,
    var commits: Int?,
    var githubId: String?,
    var id: String?,
    var name: String?,
    var profileImage: String?,
    var tier: String?,
    var rank: String?,
    var tokenAmount: Int?,
    var organization: String?,
    var organizationRank: Int?,
    var issues: Int?,
    var pullRequests: Int?,
    var review: Int?
)