package com.dragonguard.android.model.compare

//두 Repository 비교를 요청하기 위한 모델
data class CompareRepoRequestModel(
    val first_repo: String,
    val second_repo: String
)