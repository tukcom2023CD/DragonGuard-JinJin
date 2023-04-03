package com.dragonguard.android.model.menu

data class FaqModel(
    val title: String,
    val content: String,
    var expandable: Boolean = false
)
