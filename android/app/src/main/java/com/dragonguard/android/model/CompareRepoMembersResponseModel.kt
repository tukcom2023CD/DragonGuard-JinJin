package com.dragonguard.android.model

data class CompareRepoMembersResponseModel(
    val firstResult: List<FirstResult>?,
    val secondResult: List<SecondResult>?
)