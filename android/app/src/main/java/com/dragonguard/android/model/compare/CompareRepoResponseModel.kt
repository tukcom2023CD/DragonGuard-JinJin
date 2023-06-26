package com.dragonguard.android.model.compare

//두 Repository의 비교 결과를 받기 위한 모델
data class CompareRepoResponseModel(
    val first_repo: RepoStats?,
    val second_repo: RepoStats?
)