package com.dragonguard.android.model.contributors

data class GitRepoMember(
    val additions: Int?,
    val commits: Int?,
    val deletions: Int?,
    val githubId: String?,
    val isServiceMember: Boolean?,
    val profileUrl: String?
)