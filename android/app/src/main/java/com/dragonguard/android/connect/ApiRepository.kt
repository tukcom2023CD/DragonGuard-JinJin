package com.dragonguard.android.connect

import android.util.Log
import com.dragonguard.android.BuildConfig
import com.dragonguard.android.model.*
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit

/*
 서버에 요청하는 모든 api들의 호출부분
 */
class ApiRepository {
    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(1, TimeUnit.MINUTES)
        .readTimeout(15, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        .retryOnConnectionFailure(false)
        .build()

    private val retrofit = Retrofit.Builder().baseUrl(BuildConfig.api)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private var api = retrofit.create(GitRankAPI::class.java)

    fun getRepositoryNames(name: String, count: Int): ArrayList<RepoSearchResultModel> {
        var repoNames : ArrayList<RepoSearchResultModel> = arrayListOf<RepoSearchResultModel>()
        val queryMap = mutableMapOf<String, String>()
        queryMap.put("page","${count+1}")
        queryMap.put("name",name)
        queryMap.put("type","REPOSITORIES")

        Log.d("api 호출", "$count 페이지 검색")

        val repoName = api.getRepoName(queryMap)
        try{
            val result = repoName.execute()
            if(result.isSuccessful){
                repoNames = result.body()!!
            }
        }catch (e : SocketTimeoutException){
            return repoNames
        }
        return repoNames
    }


    fun getUserInfo(id: Int): UserInfoModel {
        val userInfo = api.getUserInfo(id)
        var userResult = UserInfoModel(null, null, null, null, null, null, null, null,null)
        try {
            val result = userInfo.execute()
            if (result.isSuccessful) {
                userResult = result.body()!!
            }
        } catch (e: Exception) {
            return userResult
        }
        return userResult
    }

    fun getRepoContributors(repoName: String): ArrayList<RepoContributorsItem> {
        val repoContributors = api.getRepoContributors(repoName)
        var repoContResult = arrayListOf(RepoContributorsItem(null,null,null,null))
        try{
            val result = repoContributors.execute()
            if(result.isSuccessful) {
                repoContResult = result.body()!!
            }
        } catch (e: Exception) {
            return repoContResult
        }
        return repoContResult
    }


    fun getTotalUsersRankings(page: Int, size: Int): ArrayList<TotalUsersRankingModelItem> {
        var rankingResult = ArrayList<TotalUsersRankingModelItem>()
        val queryMap = mutableMapOf<String, String>()
        queryMap.put("page","${page}")
        queryMap.put("size","$size")
        queryMap.put("sort","tokens,DESC")
        val ranking = api.getTotalUsersRanking(queryMap)
        try {
            val result = ranking.execute()
            if(result.isSuccessful) {
                rankingResult = result.body()!!
            }
        } catch (e: Exception) {
            Log.d("error", "유저랭킹 api 에러 ${e.message}")
            return rankingResult
        }
        return rankingResult
    }

    fun postRegister(body: RegisterGithubIdModel): Int {
        val register = api.postGithubId(body)
        var registerResult = 0
        try{
            val result = register.execute()
            if(result.isSuccessful) {
                registerResult = result.body()!!
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("error", "유저 등록 에러 ${e.message}")
            return registerResult
        }
        return registerResult
    }

    fun getTokenHistory(id: Int): ArrayList<TokenHistoryModelItem> {
        val tokenHistory = api.getTokenHistory(id)
        var tokenHistoryResult = arrayListOf(TokenHistoryModelItem(null,null,null,null, null))
        try {
            val result = tokenHistory.execute()
            if(result.isSuccessful) {
                tokenHistoryResult = result.body()!!
            }
        } catch (e: Exception) {
            return tokenHistoryResult
        }
        return tokenHistoryResult
    }

    fun postWalletAddress(body: WalletAddressModel): Boolean {
        val walletAddress = api.postWalletAddress(body)
        try{
            val result = walletAddress.execute()
            return result.isSuccessful
        } catch (e: Exception) {
            return false
        }
    }

    fun postWalletAuth(body: WalletAuthRequestModel): WalletAuthResponseModel  {
        var authResult = WalletAuthResponseModel(null, null,null)
        val retrofitWallet = Retrofit.Builder().baseUrl(BuildConfig.prepare)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val apiWallet = retrofitWallet.create(GitRankAPI::class.java)
        val authWallet = apiWallet.postWalletAuth(body)
        try{
            val result = authWallet.execute()
            if(result.isSuccessful) {
                authResult = result.body()!!
            }
        } catch (e: Exception) {
            return authResult
        }
        return authResult
    }

    fun getAuthResult(key: String): WalletAuthResultModel {
        var authResult = WalletAuthResultModel(null, null,null,null)
        val retrofitWallet = Retrofit.Builder().baseUrl(BuildConfig.prepare)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val apiWallet = retrofitWallet.create(GitRankAPI::class.java)
        val authWallet = apiWallet.getAuthResult(key)
        try{
            val result = authWallet.execute()
            if(result.isSuccessful) {
                authResult = result.body()!!
            }
        } catch (e: Exception) {
            return authResult
        }
        return authResult
    }

    fun postCompareRepoMembersRequest(body: CompareRepoRequestModel): CompareRepoMembersResponseModel {
        var compareRepoResult = CompareRepoMembersResponseModel(null, null)
        val compareRepoMembers = api.postCompareRepoMembers(body)
        try{
            val result = compareRepoMembers.execute()
            if(result.isSuccessful) {
                compareRepoResult = result.body()!!
            }
        } catch (e: Exception) {
            return compareRepoResult
        }
        return compareRepoResult
    }

    fun postCompareRepoRequest(body: CompareRepoRequestModel): CompareRepoResponseModel {
        var compareRepoResult = CompareRepoResponseModel(null, null)
        val compareRepo = api.postCompareRepo(body)
        try{
            val result = compareRepo.execute()
            if(result.isSuccessful) {
                compareRepoResult = result.body()!!
            }
        } catch (e: Exception) {
            return compareRepoResult
        }
        return compareRepoResult
    }
}