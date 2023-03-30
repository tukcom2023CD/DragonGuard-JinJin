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

//    서버에 사용자의 활용도 최산화하는 함수
    @POST("members/commits")
    fun postCommits(@Header("Authorization")token: String) : Call<Unit>

//
    @POST("prepare")
    @Headers("accept: application/json", "content-type: application/json")
    fun postWalletAuth(@Body auth : WalletAuthRequestModel) : Call<WalletAuthResponseModel>

//    klip wallet address 정보제공동의 후 wallet address를 받아오는 함수
    @GET("result")
    fun getAuthResult(@Query("request_key") key: String) : Call<WalletAuthResultModel>

//    사용자의 토큰부여 내역을 가져오기 위한 함수
    @GET("blockchain/{id}")
    fun getTokenHistory(@Path("id") userId: Long, @Header("Authorization")token: String) : Call<TokenHistoryModel>

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

    @GET("auth/refresh")
    fun getNewAccessToken(@Header("accessToken")access: String, @Header("refreshToken")refresh: String): Call<RefreshTokenModel>

    @GET("organizations/search")
    fun getOrgNames(@QueryMap query: Map<String, String>, @Header("Authorization")access: String): Call<OrganizationNamesModel>

    @POST("organizations")
    @Headers("accept: application/json", "content-type: application/json")
    fun postOrgRegist(@Body body: RegistOrgModel, @Header("Authorization")access: String): Call<RegistOrgResultModel>

    @POST("organizations/add-member")
    @Headers("accept: application/json", "content-type: application/json")
    fun postAddOrgMember(@Body body: AddOrgMemberModel, @Header("Authorization")access: String): Call<RegistOrgResultModel>

    @POST("email/send")
    @Headers("accept: application/json", "content-type: application/json")
    fun postAuthEmail(@Header("Authorization")access: String): Call<RegistOrgResultModel>

    @GET("email/check")
    fun getEmailAuthResult(@QueryMap query: Map<String, String> ,@Header("Authorization")access: String): Call<EmailAuthResultModel>

    @DELETE("email/{id}")
    fun deleteEmailCode(@Path("id") emailId: Long, @Header("Authorization")access: String): Call<Unit>
}