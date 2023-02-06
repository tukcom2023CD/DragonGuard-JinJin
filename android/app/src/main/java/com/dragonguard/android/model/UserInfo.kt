package com.dragonguard.android.model

data class UserInfo(
    val authStep: String?,
    val commits: Int?,
    val githubId: String?,
    val id: Int?,
    val name: String?,
    val profileImage: String?,
    val tier: String?
)