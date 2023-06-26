package com.dragonguard.android.model.token

data class RefreshTokenModel(
    val access_token: String?,
    val grant_type: String?,
    val refresh_token: String?
)