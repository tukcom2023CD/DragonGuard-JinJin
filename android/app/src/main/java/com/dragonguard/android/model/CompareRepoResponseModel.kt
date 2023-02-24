package com.dragonguard.android.model

data class CompareRepoResponseModel(
    val firstResult: List<FirstResult>?,
    val secondResult: List<SecondResult>?
)