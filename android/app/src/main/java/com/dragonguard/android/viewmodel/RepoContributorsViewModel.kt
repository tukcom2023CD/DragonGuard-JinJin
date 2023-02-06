package com.dragonguard.android.viewmodel

import com.dragonguard.android.connect.ApiRepository
import com.dragonguard.android.model.RepoContributorsItem

class RepoContributorsViewModel {
    private val repository = ApiRepository()

    fun getRepoContributors(repoName: String): ArrayList<RepoContributorsItem> {
        return repository.getRepoContributors(repoName)
    }
}