package com.dragonguard.android.model

data class WalletAuthResponseModel(
    val expiration_time: Int?,
    val request_key: String?,
    val status: String?
)