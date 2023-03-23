package com.dragonguard.android.model

data class RefreshTokenModel(
    val accessToken: String?,
    val grantType: String?,
    val refreshToken: String?
)