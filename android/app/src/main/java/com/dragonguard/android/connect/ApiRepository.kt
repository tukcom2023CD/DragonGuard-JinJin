package com.dragonguard.android.connect

import android.util.Log
import com.dragonguard.android.BuildConfig
import com.dragonguard.android.model.Result
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit

//api들 호출부분
class ApiRepository {
    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(1, TimeUnit.MINUTES)
        .readTimeout(50, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder().baseUrl(BuildConfig.api)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()


    fun searchApi(name: String, count: Int): ArrayList<Result> {
        var repoNames : ArrayList<Result> = arrayListOf<Result>()
        val api = retrofit.create(GitRankAPI::class.java)
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
        val api = retrofit.create(GitRankAPI::class.java)
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

    fun getUserCommits(id: Int) {
        val api = retrofit.create(GitRankAPI::class.java)
    }

    fun getUserRankings(id: Int) {
        val api = retrofit.create(GitRankAPI::class.java)
    }
}