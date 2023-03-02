package com.dragonguard.android.model

//addtion, deletion, commit의 stat을 담기 위한 모델
data class Statistics(
    val additionStats: RepoContributionStats,
    val commitStats: RepoContributionStats,
    val deletionStats: RepoContributionStats
)