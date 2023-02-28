package com.dragonguard.android.model

data class RepoMembersResult (
    val additions: Int,
    val commits: Int,
    val deletions: Int,
    val githubId: String
)