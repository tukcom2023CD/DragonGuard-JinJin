package com.dragonguard.android.connect

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface GitRankAPI {

    @GET("search")
    fun getRepoName(@QueryMap query: Map<String, String>) : Call<RepoName>
}