package com.dragonguard.android.model.compare

//Repository의 정보를 담기 위함 모델
data class RepoStats(
    val gitRepo: GitRepo?,
    val statistics: Statistics?,
    val languages: Map<String, Int>?,
    val languagesStats: RepoContributionStats?,
    val profileUrls: List<String>?
)
