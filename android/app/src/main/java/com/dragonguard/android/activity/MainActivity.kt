package com.dragonguard.android.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.dragonguard.android.BuildConfig
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
import java.net.*
import java.util.*
import kotlin.concurrent.scheduleAtFixedRate

/*
 사용자의 정보를 보여주고 검색, 랭킹등을
 보러가는 화면으로 이동할 수 있는 메인 activity
 */
class MainActivity : AppCompatActivity() {
    private val activityResultLauncher : ActivityResultLauncher<Intent> = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if(it.resultCode == 0 ) {
            val walletIntent = it.data
            try{
                val requestKey = walletIntent!!.getStringExtra("key")
//            Toast.makeText(applicationContext, requestKey, Toast.LENGTH_SHORT).show()
                if(!NetworkCheck.checkNetworkState(this)) {
                    Toast.makeText(applicationContext, "인터넷을 연결하세요!!", Toast.LENGTH_LONG).show()
                } else {
                    //쿠키 확인 코드
                    val cookieManager = CookieManager()
                    CookieHandler.setDefault(cookieManager)
                    val cookieList = cookieManager.cookieStore.cookies
                    for (cookie in cookieList) {
                        Log.d("cookie", "main cookies :  ${cookie.name} = ${cookie.value}")
                    }
                    authRequestResult(requestKey!!)
                }
            } catch(e: Exception) {
//                finishAffinity()
//                val intent = Intent(this, MainActivity::class.java)
//                startActivity(intent)
//                exitProcess(0)
            }

        } else if(it.resultCode == 1) {
            //쿠키 확인 코드
            val cookieManager = CookieManager()
            CookieHandler.setDefault(cookieManager)
            val cookieList = cookieManager.cookieStore.cookies
            for (cookie in cookieList) {
                Log.d("cookie", "cookies :  ${cookie.name} = ${cookie.value}")
            }
        }
    }
    companion object {
        lateinit var prefs: IdPreference
    }
    private lateinit var binding: ActivityMainBinding
    private var viewmodel = Viewmodel()
    private var backPressed : Long = 0
    private var walletAddress = ""
    private var loginOut = false
    private var addressPost = false
    private var token = ""
    private var count = 0

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Log.d("on", "onnewintent")
        val coroutine = CoroutineScope(Dispatchers.IO)
        coroutine.launch {
            val url = URL("${BuildConfig.api}")
            val connection = url.openConnection() as HttpURLConnection
            val cookieManager = CookieManager()
            cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL)
            val cookies = connection.getHeaderField("Set-Cookie")
            if (cookies != null) {
                cookieManager.cookieStore.add(url.toURI(), HttpCookie.parse(cookies)[0])
            }
            val storedCookies = cookieManager.cookieStore.get(url.toURI())
            for (cookie in storedCookies) {
                Log.d("COOKIE", cookie.toString())
            }
        }
        val logout = intent?.getBooleanExtra("logout", false)
        if(logout != null) {
            loginOut = logout
            if(loginOut) {
                prefs.setWalletAddress("wallet_address", "")
                val intent = Intent(applicationContext, LoginActivity::class.java)
                intent.putExtra("wallet_address", walletAddress)
                intent.putExtra("logout", loginOut)
                activityResultLauncher.launch(intent)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.mainViewModel = viewmodel
        prefs = IdPreference(applicationContext)
//        val intent = Intent(applicationContext, LoginActivity::class.java)
//        startActivity(intent)
        addressPost = prefs.getPostAddress("post", false)
        if(loginOut) {
            prefs.setWalletAddress("wallet_address", "")
        }

        //쿠키 확인 코드
        val coroutine = CoroutineScope(Dispatchers.IO)
        coroutine.launch {
            val url = URL("http://172.30.1.44/api")
            val connection = url.openConnection() as HttpURLConnection
            val cookieManager = CookieManager()
            cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL)
            val cookies = connection.getHeaderField("Set-Cookie")
            if (cookies != null) {
                cookieManager.cookieStore.add(url.toURI(), HttpCookie.parse(cookies)[0])
            }
            val storedCookies = cookieManager.cookieStore.get(url.toURI())
            for (cookie in storedCookies) {
                Log.d("COOKIE", "cookie $cookie")
            }
        }

        count = 0
        val jwtToken = intent?.data?.getQueryParameter("accessToken")
//        Toast.makeText(applicationContext, "jwt token : $jwtToken", Toast.LENGTH_SHORT).show()
        Log.d("jwt token", "jwt Token : $jwtToken")
        if(jwtToken != null) {
            token = jwtToken
            searchUser()
            prefs.setJwtToken("token", jwtToken)
            Log.d("nono", "main token : ${prefs.getJwtToken("token", "")}")
        } else {
            token = prefs.getJwtToken("token", "")
        }

        val intent = Intent(applicationContext, LoginActivity::class.java)
        intent.putExtra("wallet_address", walletAddress)
        intent.putExtra("logout", loginOut)
        intent.putExtra("token", prefs.getJwtToken("token", ""))
        activityResultLauncher.launch(intent)



        if(NetworkCheck.checkNetworkState(this)) {
            searchUser()
        }

//        랭킹 보러가기 버튼 눌렀을 때 랭킹 화면으로 전환
        binding.lookRanking.setOnClickListener {
            val intent = Intent(applicationContext, RankingsActivity::class.java)
            intent.putExtra("token", prefs.getJwtToken("token", ""))
            startActivity(intent)
        }

//        유저 아이디, 프로필을 눌렀을 때 메뉴 화면으로 전환
        viewmodel.onUserIconSelected.observe(this, Observer {
            if(viewmodel.onUserIconSelected.value == true){
                val intent = Intent(applicationContext, MenuActivity::class.java)
                intent.putExtra("token", prefs.getJwtToken("token", ""))
                startActivity(intent)
            }
        })

//        검색창, 검색 아이콘 눌렀을 때 검색화면으로 전환
        viewmodel.onSearchClickListener.observe(this, Observer {
            if(viewmodel.onSearchClickListener.value == true) {
                if(prefs.getJwtToken("token", "").isNotBlank()) {
                    val intent = Intent(applicationContext, SearchActivity::class.java)
                    intent.putExtra("token", prefs.getJwtToken("token", ""))
                    startActivity(intent)
                }
            }
        })

        binding.repoCompare.setOnClickListener {
            val intent = Intent(applicationContext, RepoChooseActivity::class.java)
            intent.putExtra("token", prefs.getJwtToken("token", ""))
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        searchUser()
    }

/*  메인화면의 유저 정보 검색하기(프로필 사진, 기여도, 랭킹)
    무한히 요청을 보내는 버그 해결
 */
    private fun searchUser(){
//        Toast.makeText(application, "id = $id", Toast.LENGTH_SHORT).show()
        if(token.isNotBlank()) {
            val coroutine = CoroutineScope(Dispatchers.Main)
            coroutine.launch {
                val resultDeferred = coroutine.async(Dispatchers.IO) {
                    viewmodel.getUserInfo(token)
                }
                val userInfo = resultDeferred.await()
//                Toast.makeText(applicationContext, "$userInfo", Toast.LENGTH_SHORT).show()
                if(userInfo.githubId == null || userInfo.id == null || userInfo.rank == null || userInfo.commits ==null) {
//                Toast.makeText(applicationContext, "id 비어있음", Toast.LENGTH_SHORT).show()
//                    if(prefs.getRefreshToken("refresh", "").isBlank() && count ==0) {
//                        count++
//                        Toast.makeText(applicationContext, "refresh token is blank", Toast.LENGTH_SHORT).show()
//                        loginOut = true
//                        prefs.setWalletAddress("wallet_address", "")
//                        val intent = Intent(applicationContext, LoginActivity::class.java)
//                        intent.putExtra("wallet_address", walletAddress)
//                        intent.putExtra("token", prefs.getJwtToken("token", ""))
//                        intent.putExtra("logout", true)
//                        activityResultLauncher.launch(intent)
//                    }
                } else {
                    if(userInfo.githubId.isNotBlank()) {
                        binding.userId.text = userInfo.githubId
                    }
                    if(userInfo.commits != 0 && userInfo.tier == "SPROUT" ) {
                        if(prefs.getWalletAddress("wallet_address", "") != "") {
                            postWalletAddress(prefs.getWalletAddress("wallet_address", ""))
                        } else {
                            binding.userTier.text = "내 티어 : ${userInfo.tier}"
                        }
                    } else {
                        binding.userTier.text = "내 티어 : ${userInfo.tier}"
                    }
                    if(userInfo.tokenAmount == null) {
                        binding.userToken.text = "내 기여도 : ${userInfo.commits}"
                    } else {
                        binding.userToken.text = "내 기여도 : ${userInfo.tokenAmount}"
                    }
                    binding.userRanking.text = userInfo.rank
                    if(!this@MainActivity.isFinishing) {
                        Glide.with(binding.githubProfile).load(userInfo.profileImage).into(binding.githubProfile)
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
            if(authResponse.request_key.isNullOrEmpty() || authResponse.status != "completed" || authResponse.result == null) {
//                Toast.makeText(applicationContext, "auth 결과 : 재전송", Toast.LENGTH_SHORT).show()
                val intent = Intent(applicationContext, LoginActivity::class.java)
                intent.putExtra("wallet_address", walletAddress)
                activityResultLauncher.launch(intent)

            } else {
//                Toast.makeText(applicationContext, "key : $key wallet 주소 : ${authResponse.result.klaytn_address}", Toast.LENGTH_SHORT).show()
                prefs.setWalletAddress("wallet_address", authResponse.result.klaytn_address)
                postWalletAddress(authResponse.result.klaytn_address)
            }
        }
    }

    private fun postWalletAddress(address: String) {
//        Toast.makeText(applicationContext, "address: $address", Toast.LENGTH_SHORT).show()
        if(!addressPost) {
            prefs.setPostAddress("post", true)
            addressPost = prefs.getPostAddress("post", true)
            val coroutine = CoroutineScope(Dispatchers.Main)
            coroutine.launch {
                val postwalletDeferred = coroutine.async(Dispatchers.IO) {
                    viewmodel.postWalletAddress(address, prefs.getJwtToken("token", ""))
                }
                val postWalletResponse = postwalletDeferred.await()
                if(postWalletResponse) {
                    searchUser()
                }
            }
        }

    }

//    뒤로가기 1번 누르면 종료 안내 메시지, 2번 누르면 종료
    override fun onBackPressed() {
//        super.onBackPressed()
        if(System.currentTimeMillis() > backPressed + 2500) {
            backPressed = System.currentTimeMillis()
            Toast.makeText(applicationContext, "Back 버튼을 한번 더 누르면 종료합니다.",Toast.LENGTH_SHORT).show()
            return
        }

        if(System.currentTimeMillis() <= backPressed + 2500) {
            finishAffinity()
        }
    }
}
