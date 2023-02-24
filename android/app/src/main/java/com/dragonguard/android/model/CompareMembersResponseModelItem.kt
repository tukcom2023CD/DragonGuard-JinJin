package com.dragonguard.android.model

data class CompareMembersResponseModelItem(
    val additions: Int,
    val commits: Int,
    val deletions: Int,
    val githubId: String
)