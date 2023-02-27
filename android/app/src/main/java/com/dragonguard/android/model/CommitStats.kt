package com.dragonguard.android.model

data class CommitStats(
    val average: Double,
    val count: Int,
    val max: Int,
    val min: Int,
    val sum: Int
)