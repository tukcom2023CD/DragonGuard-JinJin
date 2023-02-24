package com.dragonguard.android.connect

import com.dragonguard.android.model.*
import retrofit2.Call
import retrofit2.http.*

/*
 사용하는 모든 api의 네트워크 통신이 필요한 순간에 호출할 함수를 포함하는 인터페이스
 */
interface GitRankAPI {

//    repo 검색 함수
    @GET("search")
    fun getRepoName(@QueryMap query: Map<String, String>) : Call<RepoNameModel>

//    id에 해당하는 사용자의 티어를 받아오는 함수
    @GET("members/{id}/tier")
    fun getUserTier(@Path("id")userId : Int) : Call<String>

//    id에 해당하는 사용자의 정보를 받아오는 함수
    @GET("/api/members/{id}")
    fun getUserInfo(@Path("id")userId : Int) : Call<UserInfoModel>

//    repoName에 해당하는 repo의 정보를 받아오는 함수
    @GET("git-repos")
    fun getRepoContributors(@Query("name") repoName: String): Call<RepoContributorsModel>

//    모든 사용자들의 랭킹을 받아오는 함수
    @GET("members/ranking")
    fun getTotalUsersRanking(@QueryMap query: Map<String, String>) : Call<TotalUsersRankingModel>

//    서버에 해당 정보를 가진 사용자를 등록하는 함수
    @POST("members")
    @Headers("accept: application/json", "content-type: application/json")
    fun postGithubId(@Body register: RegisterGithubIdModel) : Call<Int>

    @POST("prepare")
    @Headers("accept: application/json", "content-type: application/json")
    fun postWalletAuth(@Body auth : WalletAuthRequestModel) : Call<WalletAuthResponseModel>

    @GET("result")
    fun getAuthResult(@Query("request_key") key: String) : Call<WalletAuthResultModel>

    @GET("blockchain/{id}")
    fun getTokenHistory(@Path("id") userId: Int) : Call<TokenHistoryModel>

    @POST("members/wallet-address")
    @Headers("accept: application/json", "content-type: application/json")
    fun postWalletAddress(@Body walletAddress : WalletAddressModel) : Call<Unit>

    @POST("git-repos/compare/git-repos-members")
    @Headers("accept: application/json", "content-type: application/json")
    fun postCompareRepoMembers(@Body compare : CompareRepoRequestModel) : Call<CompareRepoResponseModel>


}