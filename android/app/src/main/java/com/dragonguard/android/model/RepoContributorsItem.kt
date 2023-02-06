package com.dragonguard.android.model

data class RepoContributorsItem(
    val additions: Int,
    val commits: Int,
    val deletions: Int,
    val githubId: String
)