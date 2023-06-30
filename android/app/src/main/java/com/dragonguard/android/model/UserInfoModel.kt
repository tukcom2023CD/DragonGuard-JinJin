package com.dragonguard.android.model

/*
 메인화면의 사용자의 프로필, 랭킹, 기여도, 티어등을
 받기위해 정의한 model
 */
data class UserInfoModel(
    var auth_step: String?,
    var commits: Int?,
    var github_id: String?,
    var id: String?,
    var name: String?,
    var profile_image: String?,
    var tier: String?,
    var rank: Int?,
    var token_amount: Int?,
    var organization: String?,
    var organization_rank: Int?,
    var blockchain_url: String?,
    var issues: Int?,
    var pull_requests: Int?,
    var reviews: Int?,
    var is_last: Boolean?,
    var member_github_ids: List<String>?
)