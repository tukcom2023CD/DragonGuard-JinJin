package com.dragonguard.android.model

//두 Repository의 비교 결과를 받기 위한 모델
data class CompareRepoResponseModel(
    val firstRepo: RepoStats?,
    val secondRepo: RepoStats?
)