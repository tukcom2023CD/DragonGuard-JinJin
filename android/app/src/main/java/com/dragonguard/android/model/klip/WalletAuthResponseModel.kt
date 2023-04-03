package com.dragonguard.android.model.klip

//KLIP에 지갑주소 정보제공동의를 위한 request_key를 받아오는 모델
data class WalletAuthResponseModel(
    val expiration_time: Int?,
    val request_key: String?,
    val status: String?
)