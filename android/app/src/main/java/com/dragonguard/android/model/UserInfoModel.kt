package com.dragonguard.android.model

data class UserInfoModel(
    val authStep: String?,
    val commits: Int?,
    val githubId: String?,
    val id: Int?,
    val name: String?,
    val profileImage: String?,
    val tier: String?,
    val rank: String?
)