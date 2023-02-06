package com.dragonguard.android.connect

import com.dragonguard.android.model.RegisterGithubIdModel
import com.dragonguard.android.model.RepoContributorsModel
import com.dragonguard.android.model.RepoNameModel
import com.dragonguard.android.model.UserInfoModel
import retrofit2.Call
import retrofit2.http.*

interface GitRankAPI {

    @GET("search")
    fun getRepoName(@QueryMap query: Map<String, String>) : Call<RepoNameModel>

    @GET("members/{id}/tier")
    fun getUserTier(@Path("id")userId : Int) : Call<String>

    @GET("/api/members/{id}")
    fun getUserInfo(@Path("id")userId : Int) : Call<UserInfoModel>

    @GET("git-repos")
    fun getRepoContributors(@Query("name") repoName: String): Call<RepoContributorsModel>

    @GET("")
    fun getUserRanking()

    @POST("members")
    @Headers("accept: application/json", "content-type: application/json")
    fun postGithubId(@Body register: RegisterGithubIdModel) : Call<Int>
}