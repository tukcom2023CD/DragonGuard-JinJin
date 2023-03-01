package com.dragonguard.android.model

data class Statistics(
    val additionStats: RepoContributionStats,
    val commitStats: RepoContributionStats,
    val deletionStats: RepoContributionStats
)