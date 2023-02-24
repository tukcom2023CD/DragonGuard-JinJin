package com.dragonguard.android.viewmodel


import androidx.lifecycle.MutableLiveData
import com.dragonguard.android.connect.ApiRepository
import com.dragonguard.android.model.*

/*
 mvvm 구현을 위한 viewmodel
 */
class Viewmodel {
    private val repository = ApiRepository()
    var onSearchClickListener = MutableLiveData<Boolean>()
    var onUserIconSelected = MutableLiveData<Boolean>()
    var onOptionListener = MutableLiveData<String>()
    var onSearchListener = MutableLiveData<String>()

//
    fun getSearchTierResult(id: Int): UserInfoModel{
        return repository.getUserInfo(id)
    }

    fun postRegister(body: RegisterGithubIdModel): Int {
        return repository.postRegister(body)
    }

    fun getSearchRepoResult(name: String, count: Int): ArrayList<RepoSearchResultModel> {
        return repository.getRepositoryNames(name, count)
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

    fun postWalletAuth(body: WalletAuthRequestModel): WalletAuthResponseModel {
        return repository.postWalletAuth(body)
    }

    fun getWalletAuthResult(key: String): WalletAuthResultModel {
        return repository.getAuthResult(key)
    }

    fun getTokenHistory(id: Int): ArrayList<TokenHistoryModelItem> {
        return repository.getTokenHistory(id)
    }

    fun postWalletAddress(id: Int, walletAddress: String): Boolean {
        val body = WalletAddressModel(id.toString(), walletAddress)
        return repository.postWalletAddress(body)
    }

    fun postCompareRepoRequest(firstRepo: String, secondRepo: String): CompareRepoResponseModel {
        val body = CompareRepoRequestModel(firstRepo, secondRepo)
        return repository.postCompareRepoRequest(body)
    }

    fun postCompareMembers(firstName: String, secondName: String, firstRepo: String, secondRepo: String): CompareMembersResponseModel {
        val body = CompareMembersRequestModel(firstName, firstRepo, secondName, secondRepo)
        return repository.postCompareMembers(body)
    }

}