package com.dragonguard.android.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.webkit.CookieManager
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.dragonguard.android.R
import com.dragonguard.android.activity.compare.RepoChooseActivity
import com.dragonguard.android.activity.menu.MenuActivity
import com.dragonguard.android.activity.ranking.RankingsActivity
import com.dragonguard.android.activity.search.SearchActivity
import com.dragonguard.android.connect.NetworkCheck
import com.dragonguard.android.databinding.ActivityMainBinding
import com.dragonguard.android.preferences.IdPreference
import com.dragonguard.android.viewmodel.Viewmodel
import kotlinx.coroutines.*
import java.util.*

/*
 사용자의 정보를 보여주고 검색, 랭킹등을
 보러가는 화면으로 이동할 수 있는 메인 activity
 */
class MainActivity : AppCompatActivity() {
    private val activityResultLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == 0) {
                val walletIntent = it.data
                try {
                    val requestKey = walletIntent!!.getStringExtra("key")
//            Toast.makeText(applicationContext, requestKey, Toast.LENGTH_SHORT).show()
                    if (!NetworkCheck.checkNetworkState(this)) {
                        Toast.makeText(applicationContext, "인터넷을 연결하세요!!", Toast.LENGTH_LONG).show()
                    } else {
                        authRequestResult(requestKey!!)
                    }
                } catch (e: Exception) {
//                finishAffinity()
//                val intent = Intent(this, MainActivity::class.java)
//                startActivity(intent)
//                exitProcess(0)
                }

            } else if (it.resultCode == 1) {

            }
        }

    companion object {
        lateinit var prefs: IdPreference
    }

    private lateinit var binding: ActivityMainBinding
    private var viewmodel = Viewmodel()
    private var backPressed: Long = 0
    private var loginOut = false
    private var addressPost = false
    private var token = ""

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Log.d("on", "onnewintent")
        val logout = intent?.getBooleanExtra("logout", false)
        if (logout != null) {
            if(!this@MainActivity.isFinishing) {
                loginOut = logout
                if (loginOut) {
                    prefs.setWalletAddress("")
                    loginOut = true
                    prefs.setJwtToken("")
                    prefs.setRefreshToken("")
                    prefs.setPostAddress(false)
                    val intent = Intent(applicationContext, LoginActivity::class.java)
                    intent.putExtra("wallet_address", prefs.getWalletAddress(""))
                    intent.putExtra("token", prefs.getJwtToken(""))
                    intent.putExtra("logout", true)
                    activityResultLauncher.launch(intent)
                }
            }

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.mainViewModel = viewmodel
        prefs = IdPreference(applicationContext)
        CookieManager.getInstance().setAcceptCookie(true)
        if (loginOut) {
            prefs.setWalletAddress("")
        }

        val refresh = intent.getStringExtra("refresh")
        val access = intent.getStringExtra("access")
        Log.d("cookie", "webview cookie : $refresh")
        if (access != null) {
            token = access
            prefs.setJwtToken(access)
        } else {
            token = prefs.getJwtToken("")
        }
        if (refresh != null) {
            prefs.setRefreshToken(refresh)
        }
        if (token.isBlank() || prefs.getWalletAddress("").isBlank()) {
            val intent = Intent(applicationContext, LoginActivity::class.java)
            intent.putExtra("wallet_address", prefs.getWalletAddress(""))
            intent.putExtra("logout", loginOut)
            activityResultLauncher.launch(intent)
        } else {
            if (NetworkCheck.checkNetworkState(this)) {
                multipleSearchUser()
            }
        }


//        랭킹 보러가기 버튼 눌렀을 때 랭킹 화면으로 전환
        binding.lookRanking.setOnClickListener {
            val intent = Intent(applicationContext, RankingsActivity::class.java)
            intent.putExtra("token", prefs.getJwtToken(""))
            startActivity(intent)
        }

//        유저 아이디, 프로필을 눌렀을 때 메뉴 화면으로 전환
        viewmodel.onUserIconSelected.observe(this, Observer {
            if (viewmodel.onUserIconSelected.value == true) {
                val intent = Intent(applicationContext, MenuActivity::class.java)
                intent.putExtra("token", prefs.getJwtToken(""))
                startActivity(intent)
            }
        })

//        검색창, 검색 아이콘 눌렀을 때 검색화면으로 전환
        viewmodel.onSearchClickListener.observe(this, Observer {
            if (viewmodel.onSearchClickListener.value == true) {
                if (prefs.getJwtToken("").isNotBlank()) {
                    val intent = Intent(applicationContext, SearchActivity::class.java)
                    intent.putExtra("token", prefs.getJwtToken(""))
                    startActivity(intent)
                }
            }
        })

        binding.repoCompare.setOnClickListener {
            val intent = Intent(applicationContext, RepoChooseActivity::class.java)
            intent.putExtra("token", prefs.getJwtToken(""))
            startActivity(intent)
        }
    }

    override fun onRestart() {
        super.onRestart()
        multipleSearchUser()
        refreshCommits()
    }

    private fun multipleSearchUser() {
        searchUser()
        Handler(Looper.getMainLooper()).postDelayed({ searchUser() }, 500)
        Handler(Looper.getMainLooper()).postDelayed({ searchUser() }, 1000)
    }

    /*  메인화면의 유저 정보 검색하기(프로필 사진, 기여도, 랭킹)
        무한히 요청을 보내는 버그 해결
     */
    private fun searchUser() {
//        Toast.makeText(application, "id = $id", Toast.LENGTH_SHORT).show()
        if (token.isNotBlank()) {
            val coroutine = CoroutineScope(Dispatchers.Main)
            coroutine.launch {
                val resultDeferred = coroutine.async(Dispatchers.IO) {
                    viewmodel.getUserInfo(token)
                }
                val userInfo = resultDeferred.await()
//                Toast.makeText(applicationContext, "$userInfo", Toast.LENGTH_SHORT).show()
                if (userInfo.githubId == null || userInfo.id == null || userInfo.rank == null || userInfo.commits == null) {
                    if (prefs.getRefreshToken("").isBlank()) {
                        if (!this@MainActivity.isFinishing) {
                            Toast.makeText(
                                applicationContext,
                                "refresh token is blank",
                                Toast.LENGTH_SHORT
                            ).show()
                            loginOut = true
                            prefs.setJwtToken("")
                            prefs.setRefreshToken("")
                            prefs.setPostAddress(false)
                            val intent = Intent(applicationContext, LoginActivity::class.java)
                            intent.putExtra("wallet_address", prefs.getWalletAddress(""))
                            intent.putExtra("token", prefs.getJwtToken(""))
                            intent.putExtra("logout", true)
                            activityResultLauncher.launch(intent)
                        }
                    } else {
                        refreshToken()
                    }
                } else {
                    if (userInfo.githubId.isNotBlank()) {
                        binding.userId.text = userInfo.githubId
                    }
                    if (userInfo.commits != 0 && userInfo.tier == "SPROUT") {
                        if (prefs.getWalletAddress("") != "") {
                            postWalletAddress(prefs.getWalletAddress(""))
                        } else {
                            binding.userTier.text = "내 티어 : ${userInfo.tier}"
                        }
                    } else {
                        binding.userTier.text = "내 티어 : ${userInfo.tier}"
                    }
                    if (userInfo.tokenAmount == null) {
                        binding.userToken.text = "내 기여도 : ${userInfo.commits}"
                    } else {
                        binding.userToken.text = "내 기여도 : ${userInfo.tokenAmount}"
                    }
                    binding.userRanking.text = userInfo.rank
                    if (!this@MainActivity.isFinishing) {
                        Glide.with(binding.githubProfile).load(userInfo.profileImage)
                            .into(binding.githubProfile)
                    }

                }
            }
        }
    }

    private fun authRequestResult(key: String) {
        val coroutine = CoroutineScope(Dispatchers.Main)
        coroutine.launch {
            val authResponseDeferred = coroutine.async(Dispatchers.IO) {
                viewmodel.getWalletAuthResult(key)
            }
            val authResponse = authResponseDeferred.await()
            if (authResponse.request_key.isNullOrEmpty() || authResponse.status != "completed" || authResponse.result == null) {
//                Toast.makeText(applicationContext, "auth 결과 : 재전송", Toast.LENGTH_SHORT).show()
                if(!this@MainActivity.isFinishing) {
                    val intent = Intent(applicationContext, LoginActivity::class.java)
                    intent.putExtra("wallet_address", prefs.getWalletAddress(""))
                    activityResultLauncher.launch(intent)
                }
            } else {
//                Toast.makeText(applicationContext, "key : $key wallet 주소 : ${authResponse.result.klaytn_address}", Toast.LENGTH_SHORT).show()
                prefs.setWalletAddress(authResponse.result.klaytn_address)
                postWalletAddress(authResponse.result.klaytn_address)
            }
        }
    }

    private fun postWalletAddress(address: String) {
//        Toast.makeText(applicationContext, "address: $address", Toast.LENGTH_SHORT).show()
        if (!addressPost) {
            prefs.setPostAddress(true)
            addressPost = prefs.getPostAddress(true)
            val coroutine = CoroutineScope(Dispatchers.Main)
            coroutine.launch {
                val postwalletDeferred = coroutine.async(Dispatchers.IO) {
                    viewmodel.postWalletAddress(address, prefs.getJwtToken(""))
                }
                val postWalletResponse = postwalletDeferred.await()
                if (postWalletResponse) {
                    multipleSearchUser()
                }
            }
        }
    }

    private fun refreshToken() {
        val coroutine = CoroutineScope(Dispatchers.Main)
        coroutine.launch {
            val refreshDeffered = coroutine.async(Dispatchers.IO) {
                viewmodel.getNewToken(prefs.getJwtToken(""), prefs.getRefreshToken(""))
            }
            val refresh = refreshDeffered.await()
            if (refresh.refreshToken != null && refresh.accessToken != null) {
                prefs.setJwtToken(refresh.accessToken)
                prefs.setRefreshToken(refresh.refreshToken)
                token = refresh.accessToken
                multipleSearchUser()
            }
        }
    }

    private fun refreshCommits() {
        if (prefs.getRefreshToken("").isNotBlank()) {
            Log.d("post", "refresh commits")
            val coroutine = CoroutineScope(Dispatchers.Main)
            coroutine.launch {
                val refreshDeffered = coroutine.async(Dispatchers.IO) {
                    viewmodel.postCommits(prefs.getJwtToken(""))
                }
                val refresh = refreshDeffered.await()
                multipleSearchUser()
            }
        }

    }

    //    뒤로가기 1번 누르면 종료 안내 메시지, 2번 누르면 종료
    override fun onBackPressed() {
//        super.onBackPressed()
        if (System.currentTimeMillis() > backPressed + 2500) {
            backPressed = System.currentTimeMillis()
            Toast.makeText(applicationContext, "Back 버튼을 한번 더 누르면 종료합니다.", Toast.LENGTH_SHORT)
                .show()
            return
        }

        if (System.currentTimeMillis() <= backPressed + 2500) {
            finishAffinity()
        }
    }
}
