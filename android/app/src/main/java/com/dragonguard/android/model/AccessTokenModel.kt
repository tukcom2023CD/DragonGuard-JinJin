package com.dragonguard.android.model

data class AccessTokenModel(
    val access_token: String?,
    val scope: String?,
    val token_type: String?
)
