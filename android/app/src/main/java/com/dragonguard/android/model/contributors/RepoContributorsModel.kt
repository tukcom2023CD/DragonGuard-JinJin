package com.dragonguard.android.model.contributors

data class RepoContributorsModel(
    val git_repo_members: List<GitRepoMember>?,
    val sparkLine: List<Int>?
)