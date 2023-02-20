package com.dragonguard.android.model

data class WalletAuthResultModel(
    val expiration_time: Int?,
    val request_key: String?,
    val result: Result?,
    val status: String?
)