package com.dragonguard.android.model.compare

//addtion, deletion, commit의 stat을 담기 위한 모델
data class Statistics(
    val addition_stats: RepoContributionStats,
    val commit_stats: RepoContributionStats,
    val deletion_stats: RepoContributionStats
)