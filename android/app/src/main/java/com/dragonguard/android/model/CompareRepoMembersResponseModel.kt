package com.dragonguard.android.model

data class CompareRepoMembersResponseModel(
    val firstResult: List<RepoMembersResult>?,
    val secondResult: List<RepoMembersResult>?
)