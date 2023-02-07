package com.dragonguard.android.connect

import android.util.Log
import com.dragonguard.android.BuildConfig
import com.dragonguard.android.model.RegisterGithubIdModel
import com.dragonguard.android.model.RepoContributorsItem
import com.dragonguard.android.model.RepoSearchResultModel
import com.dragonguard.android.model.UserInfoModel
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit

//api들 호출부분
class ApiRepository {
    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(1, TimeUnit.MINUTES)
        .readTimeout(15, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder().baseUrl(BuildConfig.api)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private var api = retrofit.create(GitRankAPI::class.java)

    fun searchApi(name: String, count: Int): ArrayList<RepoSearchResultModel> {
        var repoNames : ArrayList<RepoSearchResultModel> = arrayListOf<RepoSearchResultModel>()
        val queryMap = mutableMapOf<String, String>()
        queryMap.put("page","${count+1}")
        queryMap.put("name",name)
        queryMap.put("type","repositories")

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

    fun getTier(id: Int): String {
        val tier = api.getUserTier(id)
        var tierResult = ""
        try{
            val result = tier.execute()
            if(result.isSuccessful){
                tierResult = result.body()!!
            }
        }catch (e : SocketTimeoutException){
            return tierResult
        }
        return tierResult
    }

    fun getUserInfo(id: Int): UserInfoModel {
        val userInfo = api.getUserInfo(id)
        var userResult = UserInfoModel(null, null, null, null, null, null, null, null)
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
        var repoContResult = ArrayList<RepoContributorsItem>()
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

    fun getUserCommits(id: Int) {
    }

    fun getUserRankings(id: Int) {
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
            Log.d("error", "${e.message}")
            return registerResult
        }
        return registerResult

    }
}