package com.dragonguard.android.model

data class GitRepo(
    val forks_count: Int,
    val full_name: String,
    val language: String,
    val open_issues_count: Int,
    val stargazers_count: Int,
    val subscribers_count: Int,
    val watchers_count: Int
)