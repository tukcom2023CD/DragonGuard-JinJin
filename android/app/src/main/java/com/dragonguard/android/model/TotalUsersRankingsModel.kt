package com.dragonguard.android.model

// 유저들의 랭킹 대입을 쉽게 하기 위한 Model
data class TotalUsersRankingsModel(
    val commits: Int?,
    val githubId: String?,
    val id: Int?,
    val name: String?,
    val tier: String?,
    val ranking: Int?
)
