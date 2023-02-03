package com.dragonguard.android.connect

import com.dragonguard.android.model.RepoName
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface GitRankAPI {

    @GET("search")
    fun getRepoName(@QueryMap query: Map<String, String>) : Call<RepoName>

    @GET("members/{id}/tier")
    fun getUserTier(@Path("id")userId : Int) : Call<String>

    @GET("")
    fun getUserCommits()

    @GET("")
    fun getUserRanking()
}