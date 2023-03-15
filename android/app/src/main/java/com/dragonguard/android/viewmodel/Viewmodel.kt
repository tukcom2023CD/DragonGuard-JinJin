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

    fun getSearchRepoResult(name: String, count: Int, type: String): ArrayList<RepoSearchResultModel> {
        return repository.getRepositoryNames(name, count, type)
    }

    fun getRepositoryNamesWithFilters(name:String, count:Int, filters: String, type: String):ArrayList<RepoSearchResultModel> {
        return repository.getRepositoryNamesWithFilters(name, count, filters, type)
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
        if(onOptionListener.value == "up"){
            onOptionListener.value = "down"
        }else{
            onOptionListener.value = "up"
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

    fun postCompareRepoMembersRequest(firstRepo: String, secondRepo: String): CompareRepoMembersResponseModel {
        val body = CompareRepoRequestModel(firstRepo, secondRepo)
        return repository.postCompareRepoMembersRequest(body)
    }

    fun postCompareRepoRequest(firstRepo: String, secondRepo: String): CompareRepoResponseModel {
        val body = CompareRepoRequestModel(firstRepo, secondRepo)
        return repository.postCompareRepoRequest(body)
    }

    fun getOauthToken(code: String):AccessTokenModel {
        return repository.getAccessToken(code)
    }

    fun getOauthUserInfo(token: String): OauthUserInfoModel? {
        return repository.getOauthUserInfo(token)
    }
}