package com.dragonguard.android.connect

import android.util.Log
import com.dragonguard.android.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

//api들 호출부분
class ApiCall {
    fun searchApi(name: String, count: Int): ArrayList<Result> {

        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(50, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .build()

        val searchRetrofit = Retrofit.Builder().baseUrl(BuildConfig.server)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        var repoNames : ArrayList<Result> = arrayListOf<Result>()

        val api = searchRetrofit.create(GitRankAPI::class.java)
        val queryMap = mutableMapOf<String, String>()
        queryMap.put("page","${count+1}")
        queryMap.put("name",name)
        queryMap.put("type","repositories")

        Log.d("api 호출", "$count 페이지 검색")
        val repoName = api.getRepoName(queryMap)
        val result = repoName.execute()
        if(result.isSuccessful){
            repoNames = result.body()!!.result as ArrayList<Result>
        }
        return repoNames
    }
}