package com.dragonguard.android.model

data class RepoStats(
    val gitRepo: GitRepo?,
    val statistics: Statistics?,
    val languages: Map<String, Int>?,
    val languagesStats: RepoContributionStats?
)
