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
    fun getRepoName(@QueryMap query: Map<String, String>, @Header("Authorization")token: String) : Call<RepoNameModel>

//    id에 해당하는 사용자의 정보를 받아오는 함수
    @GET("members/me")
    fun getUserInfo(@Header("Authorization")token: String) : Call<UserInfoModel>

//    repoName에 해당하는 repo의 정보를 받아오는 함수
    @GET("git-repos")
    fun getRepoContributors(@Query("name") repoName: String, @Header("Authorization")token: String): Call<RepoContributorsModel>

//    모든 사용자들의 랭킹을 받아오는 함수
    @GET("members/ranking")
    fun getTotalUsersRanking(@QueryMap query: Map<String, String>, @Header("Authorization")token: String) : Call<TotalUsersRankingModel>

//    서버에 해당 정보를 가진 사용자를 등록하는 함수
    @POST("members")
    @Headers("accept: application/json", "content-type: application/json")
    fun postGithubId(@Body register: RegisterGithubIdModel) : Call<RegisterGithubIdResponseModel>

//
    @POST("prepare")
    @Headers("accept: application/json", "content-type: application/json")
    fun postWalletAuth(@Body auth : WalletAuthRequestModel) : Call<WalletAuthResponseModel>

//    klip wallet address 정보제공동의 후 wallet address를 받아오는 함수
    @GET("result")
    fun getAuthResult(@Query("request_key") key: String) : Call<WalletAuthResultModel>

//    사용자의 토큰부여 내역을 가져오기 위한 함수
    @GET("blockchain/{id}")
    fun getTokenHistory(@Path("id") userId: Int, @Header("Authorization")token: String) : Call<TokenHistoryModel>

//    klip wallet address를 서버로 보내는 함수
    @POST("members/wallet-address")
    @Headers("accept: application/json", "content-type: application/json")
    fun postWalletAddress(@Body walletAddress : WalletAddressModel, @Header("Authorization")token: String) : Call<Unit>

//    두 Repository의 구성원들의 정보를 받아오기 위한 함수
    @POST("git-repos/compare/git-repos-members")
    @Headers("accept: application/json", "content-type: application/json")
    fun postCompareRepoMembers(@Body compare : CompareRepoRequestModel, @Header("Authorization")token: String) : Call<CompareRepoMembersResponseModel>

//    두 Repository의 정보를 받아오기 위한 함수
    @POST("git-repos/compare")
    @Headers("accept: application/json", "content-type: application/json")
    fun postCompareRepo(@Body compare: CompareRepoRequestModel, @Header("Authorization")token: String) : Call<CompareRepoResponseModel>

    @POST("access_token")
    @Headers("accept: application/json", "content-type: application/json")
    fun getAccessToken(@QueryMap query: Map<String, String>): Call<AccessTokenModel>

    @POST("user")
    @Headers("accept: application/json", "content-type: application/json")
    fun getOauthUserInfo(@Header("Authorization")token: String): Call<OauthUserInfoModel>

    @GET("auth/refresh")
    fun getNewAccessToken(): Call<String>

}