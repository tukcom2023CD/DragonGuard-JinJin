package com.dragonguard.android.model.contributors

data class GitRepoMember(
    val additions: Int?,
    val commits: Int?,
    val deletions: Int?,
    val github_id: String?,
    val is_service_member: Boolean?,
    val profile_url: String?
)