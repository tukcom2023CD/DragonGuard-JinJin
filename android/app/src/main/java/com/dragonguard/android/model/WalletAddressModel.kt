package com.dragonguard.android.model

//서버에 지갑주소를 전송하기위한 모델
data class WalletAddressModel(
    val id: String,
    val walletAddress: String
)