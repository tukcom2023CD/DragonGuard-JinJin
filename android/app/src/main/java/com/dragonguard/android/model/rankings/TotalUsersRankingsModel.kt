package com.dragonguard.android.model.rankings

// 유저들의 랭킹 대입을 쉽게 하기 위한 Model
data class TotalUsersRankingsModel(
    val tokens: Long?,
    val github_id: String?,
    val id: String?,
    val name: String?,
    val tier: String?,
    val ranking: Int?,
    val profile_image: String?
)
