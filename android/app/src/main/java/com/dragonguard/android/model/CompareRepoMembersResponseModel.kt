package com.dragonguard.android.model

//두 Repository의 멤버의 상세정보를 담기 위한 모델
data class CompareRepoMembersResponseModel(
    val firstResult: List<RepoMembersResult>?,
    val secondResult: List<RepoMembersResult>?
)