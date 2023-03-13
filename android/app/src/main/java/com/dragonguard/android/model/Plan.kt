package com.dragonguard.android.model

data class Plan(
    val collaborators: Int,
    val name: String,
    val private_repos: Int,
    val space: Int
)