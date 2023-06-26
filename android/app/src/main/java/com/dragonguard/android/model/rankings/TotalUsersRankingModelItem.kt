package com.dragonguard.android.model.rankings

/*
 모든 사용자의 랭킹을 받기위해 정의한 model
 */
data class TotalUsersRankingModelItem(
    val tokens: Long?,
    val github_id: String?,
    val id: String?,
    val name: String?,
    val tier: String?,
    val profile_image: String?
)