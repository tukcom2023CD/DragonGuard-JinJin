package com.dragonguard.android.viewmodel


import android.util.Log
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
    fun getUserInfo(token: String): UserInfoModel{
        Log.d("token", "Bearer : $token")
        return repository.getUserInfo(token)
    }

    fun postRegister(body: RegisterGithubIdModel): Int {
        return repository.postRegister(body)
    }

    fun getSearchRepoResult(name: String, count: Int, type: String, token: String): ArrayList<RepoSearchResultModel> {
        return repository.getRepositoryNames(name, count, type, token)
    }

    fun getRepositoryNamesWithFilters(name:String, count:Int, filters: String, type: String, token: String):ArrayList<RepoSearchResultModel> {
        return repository.getRepositoryNamesWithFilters(name, count, filters, type, token)
    }


    fun clickUserIcon() {
        onUserIconSelected.value = true
    }

    fun clickSearchIcon() {
        onSearchClickListener.value = true
    }

    fun getRepoContributors(repoName: String, token: String): ArrayList<RepoContributorsItem> {
        return repository.getRepoContributors(repoName, token)
    }

    fun clickSearchOption(){
        if(onOptionListener.value == "up"){
            onOptionListener.value = "down"
        }else{
            onOptionListener.value = "up"
        }
    }

    fun getTotalUserRanking(page: Int, size: Int, token: String): ArrayList<TotalUsersRankingModelItem> {
        return repository.getTotalUsersRankings(page, size, token)
    }

    fun postWalletAuth(body: WalletAuthRequestModel): WalletAuthResponseModel {
        return repository.postWalletAuth(body)
    }

    fun getWalletAuthResult(key: String): WalletAuthResultModel {
        return repository.getAuthResult(key)
    }

    fun getTokenHistory(id: Int, token: String): ArrayList<TokenHistoryModelItem> {
        return repository.getTokenHistory(id, token)
    }

    fun postWalletAddress(walletAddress: String, token: String): Boolean {
        val body = WalletAddressModel(walletAddress)
        return repository.postWalletAddress(body, token)
    }

    fun postCompareRepoMembersRequest(firstRepo: String, secondRepo: String, token: String): CompareRepoMembersResponseModel {
        val body = CompareRepoRequestModel(firstRepo, secondRepo)
        return repository.postCompareRepoMembersRequest(body, token)
    }

    fun postCompareRepoRequest(firstRepo: String, secondRepo: String, token: String): CompareRepoResponseModel {
        val body = CompareRepoRequestModel(firstRepo, secondRepo)
        return repository.postCompareRepoRequest(body, token)
    }

    fun getOauthToken(code: String):AccessTokenModel {
        return repository.getAccessToken(code)
    }

    fun getOauthUserInfo(token: String): OauthUserInfoModel? {
        return repository.getOauthUserInfo(token)
    }
}