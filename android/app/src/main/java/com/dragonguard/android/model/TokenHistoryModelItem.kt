package com.dragonguard.android.model

//토큰 부여 기록을 받아오기위한 모델
data class TokenHistoryModelItem(
    val amount: Int?,
    val contributeType: String?,
    val githubId: String?,
    val id: Int?,
    val memberId: Int?
)