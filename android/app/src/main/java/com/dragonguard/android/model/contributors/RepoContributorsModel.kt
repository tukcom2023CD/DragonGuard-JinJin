package com.dragonguard.android.model.contributors

data class RepoContributorsModel(
    val gitRepoMembers: List<GitRepoMember>?,
    val sparkLine: List<Int>?
)