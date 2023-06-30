package com.dragonguard.android.model.detail

data class UserProfileModel(
    val commits: Int,
    val git_repos: List<String>,
    val issues: Int,
    val organization: String?,
    val profile_image: String,
    val pull_requests: Int,
    val rank: Int,
    val reviews: Int
)