package com.dragonguard.android.connect

import com.dragonguard.android.model.*
import com.dragonguard.android.model.compare.CompareRepoMembersResponseModel
import com.dragonguard.android.model.compare.CompareRepoRequestModel
import com.dragonguard.android.model.compare.CompareRepoResponseModel
import com.dragonguard.android.model.contributors.RepoContributorsModel
import com.dragonguard.android.model.detail.ClientDetailModel
import com.dragonguard.android.model.detail.UserProfileModel
import com.dragonguard.android.model.klip.*
import com.dragonguard.android.model.org.*
import com.dragonguard.android.model.rankings.OrgInternalRankingModel
import com.dragonguard.android.model.rankings.OrganizationRankingModel
import com.dragonguard.android.model.rankings.TotalUsersRankingModel
import com.dragonguard.android.model.search.RepoNameModel
import com.dragonguard.android.model.search.UserNameModel
import com.dragonguard.android.model.token.RefreshTokenModel
import retrofit2.Call
import retrofit2.http.*

/*
 사용하는 모든 api의 네트워크 통신이 필요한 순간에 호출할 함수를 포함하는 인터페이스
 */
interface GitRankAPI {

//    repo 검색 함수
    @GET("search")
    fun getRepoName(@QueryMap query: Map<String, String>, @Header("Authorization")token: String) : Call<RepoNameModel>

    @GET("search")
    fun getUserName(@QueryMap query: Map<String, String>, @Header("Authorization")token: String) : Call<UserNameModel>

//    id에 해당하는 사용자의 정보를 받아오는 함수
    @GET("members/me")
    fun getUserInfo(@Header("Authorization")token: String) : Call<UserInfoModel>

    @POST("members/me/update")
    fun userInfoUpdate(@Header("Authorization")token: String) : Call<UserInfoModel>

//    repoName에 해당하는 repo의 정보를 받아오는 함수
    @GET("git-repos")
    fun getRepoContributors(@Query("name") repoName: String, @Header("Authorization")token: String): Call<RepoContributorsModel>

    @GET("git-repos/update")
    fun getRepoContributorsUpdate(@Query("name") repoName: String, @Header("Authorization")token: String): Call<RepoContributorsModel>

//    모든 사용자들의 랭킹을 받아오는 함수
    @GET("members/ranking")
    fun getTotalUsersRanking(@QueryMap query: Map<String, String>, @Header("Authorization")token: String) : Call<TotalUsersRankingModel>

//    서버에 사용자의 활용도 최산화하는 함수
    @POST("members/contributions")
    fun postCommits(@Header("Authorization")token: String) : Call<Unit>

    @GET("members/git-organizations/git-repos")
    fun getOrgRepoList(@Query("name") orgName: String, @Header("Authorization")token: String): Call<GithubOrgReposModel>

    @GET("members/me/details")
    fun getMemberDetails(@Header("Authorization")token: String) : Call<ClientDetailModel>

//
    @POST("prepare")
    @Headers("accept: application/json", "content-type: application/json")
    fun postWalletAuth(@Body auth : WalletAuthRequestModel) : Call<WalletAuthResponseModel>

//    klip wallet address 정보제공동의 후 wallet address를 받아오는 함수
    @GET("result")
    fun getAuthResult(@Query("request_key") key: String) : Call<WalletAuthResultModel>

//    사용자의 토큰부여 내역을 가져오기 위한 함수
    @GET("blockchain")
    fun getTokenHistory(@Header("Authorization")token: String) : Call<TokenHistoryModel>

    @POST("blockchain/update")
    fun updateToken(@Header("Authorization")token: String) : Call<TokenHistoryModel>

//    klip wallet address를 서버로 보내는 함수
    @POST("members/wallet-address")
    @Headers("accept: application/json", "content-type: application/json")
    fun postWalletAddress(@Body walletAddress : WalletAddressModel, @Header("Authorization")token: String) : Call<Unit>

//    두 Repository의 구성원들의 정보를 받아오기 위한 함수
    @POST("git-repos/compare/git-repos-members")
    @Headers("accept: application/json", "content-type: application/json")
    fun postCompareRepoMembers(@Body compare : CompareRepoRequestModel, @Header("Authorization")token: String) : Call<CompareRepoMembersResponseModel>

    @POST("git-repos/compare/git-repos-members/update")
    @Headers("accept: application/json", "content-type: application/json")
    fun postCompareRepoMembersUpdate(@Body compare : CompareRepoRequestModel, @Header("Authorization")token: String) : Call<CompareRepoMembersResponseModel>

//    두 Repository의 정보를 받아오기 위한 함수
    @POST("git-repos/compare")
    @Headers("accept: application/json", "content-type: application/json")
    fun postCompareRepo(@Body compare: CompareRepoRequestModel, @Header("Authorization")token: String) : Call<CompareRepoResponseModel>

    @POST("git-repos/compare")
    @Headers("accept: application/json", "content-type: application/json")
    fun postCompareRepoUpdate(@Body compare: CompareRepoRequestModel, @Header("Authorization")token: String) : Call<CompareRepoResponseModel>

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

    @GET("organizations/search-id")
    fun getOrgId(@Query("name") key: String, @Header("Authorization")access: String): Call<RegistOrgResultModel>

    @GET("members/ranking/organization")
    fun getOrgInternalRankings(@QueryMap query: Map<String, String> ,@Header("Authorization")access: String): Call<OrgInternalRankingModel>

    @GET("organizations/ranking")
    fun getOrgRankings(@QueryMap query: Map<String, String> ,@Header("Authorization")access: String): Call<OrganizationRankingModel>

    @GET("organizations/ranking/all")
    fun getAllOrgRankings(@QueryMap query: Map<String, String>, @Header("Authorization")access: String): Call<OrganizationRankingModel>


    @GET("admin/check")
    fun getPermissionState(@Header("Authorization")access: String): Call<Unit>

    @POST("admin/organizations/decide")
    fun postOrgApproval(@Body body: OrgApprovalModel, @Header("Authorization")access: String): Call<ApproveRequestOrgModel>

    @GET("admin/organizations")
    fun getOrgStatus(@QueryMap query: Map<String, String>, @Header("Authorization")access: String): Call<ApproveRequestOrgModel>

    @GET("members/details")
    fun getOthersProfile(@Query("githubId") query: String, @Header("Authorization")access: String): Call<UserProfileModel>

    @GET("members/verify")
    fun getLoginAuthState(@Header("Authorization")access: String): Call<AuthStateModel>

    @DELETE("members/withdraw")
    fun postWithDraw(@Header("Authorization")access: String): Call<Unit>
}