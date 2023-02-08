package com.dragonguard.android.viewmodel


import androidx.lifecycle.MutableLiveData
import com.dragonguard.android.connect.ApiRepository
import com.dragonguard.android.model.*


class Viewmodel {
    private val repository = ApiRepository()
    var onSearchClickListener = MutableLiveData<Boolean>()
    var onUserIconSelected = MutableLiveData<Boolean>()
    var onOptionListener = MutableLiveData<String>()
    var onSearchListener = MutableLiveData<String>()

    fun getSearchTierResult(id: Int): UserInfoModel{
        return repository.getUserInfo(id)
    }

    fun postRegister(body: RegisterGithubIdModel): Int {
        return repository.postRegister(body)
    }

    fun getSearchRepoResult(name: String, count: Int): ArrayList<RepoSearchResultModel> {
        return repository.searchApi(name, count)
    }


    fun clickUserIcon() {
        onUserIconSelected.value = true
    }

    fun clickSearchIcon() {
        onSearchClickListener.value = true
    }

    fun getRepoContributors(repoName: String): ArrayList<RepoContributorsItem> {
        return repository.getRepoContributors(repoName)
    }

    fun clickSearchOption(){
        if(onOptionListener.value == "down"){
            onOptionListener.value = "up"
        }else{
            onOptionListener.value = "down"
        }
    }

    fun getTotalUserRanking(page: Int, size: Int): ArrayList<TotalUsersRankingModelItem> {
        return repository.getTotalUsersRankings(page, size)
    }

}