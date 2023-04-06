package com.dragonguard.android.model.contributors

/*
 repo 정보의 내용을 받기위해 정의한 model
 */
data class RepoContributorsItem(
    val additions: Int?,
    val commits: Int?,
    val deletions: Int?,
    val githubId: String?
)