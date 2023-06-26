package com.dragonguard.android.model.compare

//Repository의 정보를 담기 위함 모델
data class RepoStats(
    val git_repo: GitRepo?,
    val statistics: Statistics?,
    val languages: Map<String, Int>?,
    val languages_stats: RepoContributionStats?,
    val profile_urls: List<String>?
)
