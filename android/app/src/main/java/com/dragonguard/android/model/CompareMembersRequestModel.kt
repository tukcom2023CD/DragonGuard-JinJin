package com.dragonguard.android.model

data class CompareMembersRequestModel(
    val firstName: String,
    val firstRepo: String,
    val secondName: String,
    val secondRepo: String
)