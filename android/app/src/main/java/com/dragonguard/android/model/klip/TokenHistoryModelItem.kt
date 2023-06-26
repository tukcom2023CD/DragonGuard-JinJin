package com.dragonguard.android.model.klip

//토큰 부여 기록을 받아오기위한 모델
data class TokenHistoryModelItem(
    val amount: Int?,
    val contribute_type: String?,
    val github_id: String?,
    val id: Long?,
    val member_id: String?,
    val created_at: String?,
    val transaction_hash_url: String?
)