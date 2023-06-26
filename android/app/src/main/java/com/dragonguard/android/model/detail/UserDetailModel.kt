package com.dragonguard.android.model.detail

data class UserDetailModel(
    val git_organizations: List<GitOrganization>?,
    val git_repos: List<String>?,
    val member_profile_image: String?
)