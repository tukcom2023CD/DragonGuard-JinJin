package com.dragonguard.android.model.klip

//KLIP에서 지갑주소를 받아오는 모델
data class WalletAuthResultModel(
    val expiration_time: Int?,
    val request_key: String?,
    val result: Result?,
    val status: String?
)