package com.dragonguard.android.model

import java.io.Serializable

/*
 repo 이름을 받기위해 정의한 model
 */
data class RepoSearchResultModel(
    val id: Long,
    val name: String
):Serializable