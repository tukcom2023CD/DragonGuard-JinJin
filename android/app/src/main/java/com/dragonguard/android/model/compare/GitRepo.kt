package com.dragonguard.android.model.compare

//Repository의 fork수 등을 담기 위한 모델
data class GitRepo(
    val forks_count: Int,
    val full_name: String,
    val closed_issues_count: Int,
    val open_issues_count: Int,
    val stargazers_count: Int,
    val subscribers_count: Int,
    val watchers_count: Int
)