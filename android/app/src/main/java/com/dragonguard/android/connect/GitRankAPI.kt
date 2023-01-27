package com.dragonguard.android.connect

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface GitRankAPI {

    @GET("{page}&name={repoName}&type={type}")
    fun getRepoName(@Path("page") pageNum: Int, @Path("repoName") name: String, @Path("type") type: String) : Call<List<RepoName>>
}