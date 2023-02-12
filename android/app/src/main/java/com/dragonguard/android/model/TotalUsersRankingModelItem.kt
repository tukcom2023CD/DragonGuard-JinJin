package com.dragonguard.android.model

/*
 모든 사용자의 랭킹을 받기위해 정의한 model
 */
data class TotalUsersRankingModelItem(
    val commits: Int?,
    val githubId: String?,
    val id: Int?,
    val name: String?,
    val tier: String?
)