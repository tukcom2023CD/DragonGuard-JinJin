package com.dragonguard.android.model.detail

data class UserDetailModel(
    val gitOrganizations: List<GitOrganization>?,
    val gitRepos: List<String>?,
    val memberProfileImage: String?
)