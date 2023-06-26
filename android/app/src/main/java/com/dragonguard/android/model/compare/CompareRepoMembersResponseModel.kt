package com.dragonguard.android.model.compare

//두 Repository의 멤버의 상세정보를 담기 위한 모델
data class CompareRepoMembersResponseModel(
    val first_result: List<RepoMembersResult>?,
    val second_result: List<RepoMembersResult>?
)