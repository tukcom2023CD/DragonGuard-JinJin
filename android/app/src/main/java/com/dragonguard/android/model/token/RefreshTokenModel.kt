package com.dragonguard.android.model.token

data class RefreshTokenModel(
    val accessToken: String?,
    val grantType: String?,
    val refreshToken: String?
)