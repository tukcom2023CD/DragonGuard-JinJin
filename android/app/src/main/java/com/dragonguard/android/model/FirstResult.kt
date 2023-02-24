package com.dragonguard.android.model

data class FirstResult(
    val additions: Int,
    val commits: Int,
    val deletions: Int,
    val githubId: String
)