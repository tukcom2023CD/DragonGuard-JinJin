package com.dragonguard.android.model

data class SecondRepo(
    val gitRepo: GitRepo?,
    val statistics: Statistics?,
    val languages: Map<String, Int>?,
    val languagesStat: LanguagesStat?
)