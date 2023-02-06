package com.dragonguard.android.connect

import com.dragonguard.android.model.RegisterGithubId
import com.dragonguard.android.model.RepoName
import com.dragonguard.android.model.UserInfo
import retrofit2.Call
import retrofit2.http.*

interface GitRankAPI {

    @GET("search")
    fun getRepoName(@QueryMap query: Map<String, String>) : Call<RepoName>

    @GET("members/{id}/tier")
    fun getUserTier(@Path("id")userId : Int) : Call<String>

    @GET("/api/members/{id}")
    fun getUserInfo(@Path("id")userId : Int) : Call<UserInfo>

    @GET("")
    fun getUserRanking()

    @POST("members")
    @Headers("accept: application/json", "content-type: application/json")
    fun postGithubId(@Body register: RegisterGithubId) : Call<Int>
}