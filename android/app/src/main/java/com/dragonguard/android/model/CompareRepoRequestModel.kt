package com.dragonguard.android.model

//두 Repository 비교를 요청하기 위한 모델
data class CompareRepoRequestModel(
    val firstRepo: String,
    val secondRepo: String
)