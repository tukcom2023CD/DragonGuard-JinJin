package com.dragonguard.android.model.compare

//addition, deletion, language의 stat을 담기 위한 모델
data class RepoContributionStats (
    val average: Double,
    val count: Int,
    val max: Int,
    val min: Int,
    val sum: Int
)