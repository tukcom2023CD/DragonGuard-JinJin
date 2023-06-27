package com.dragonguard.android.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.dragonguard.android.R
import com.dragonguard.android.activity.search.SearchActivity
import com.dragonguard.android.connect.NetworkCheck
import com.dragonguard.android.databinding.ActivityMainBinding
import com.dragonguard.android.fragment.MainFragment
import com.dragonguard.android.fragment.RankingFragment
import com.dragonguard.android.model.UserInfoModel
import com.dragonguard.android.preferences.IdPreference
import com.dragonguard.android.viewmodel.Viewmodel
import kotlinx.coroutines.*

/*
 사용자의 정보를 보여주고 검색, 랭킹등을
 보러가는 화면으로 이동할 수 있는 메인 activity
 */
class MainActivity : AppCompatActivity() {
    private val activityResultLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            count = 0
            if (it.resultCode == 0) {
                val walletIntent = it.data
                try {
                    val requestKey = walletIntent!!.getStringExtra("key")
                    val accessToken = walletIntent!!.getStringExtra("access")
                    val refreshToken = walletIntent!!.getStringExtra("refresh")
//            Toast.makeText(applicationContext, requestKey, Toast.LENGTH_SHORT).show()
                    if (!NetworkCheck.checkNetworkState(this)) {
                        Toast.makeText(applicationContext, "인터넷을 연결하세요!!", Toast.LENGTH_LONG).show()
                    } else {
//                        Toast.makeText(applicationContext, "access : $accessToken, refresh : $refreshToken", Toast.LENGTH_SHORT).show()
                        token = accessToken!!
                        prefs.setJwtToken(accessToken)
                        prefs.setRefreshToken(refreshToken!!)
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
    private var refreshState = true
    private var state = true
    private var count = 0
    private var realCount = 0
    private var refreshCount = 0
    private var mainFrag: MainFragment? = null
    private var rankingFrag: RankingFragment? = null
    private var added = false
    private var realModel = UserInfoModel(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null)
    private var finish = false
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        count = 0
        finish = false
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
        count = 0
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.mainViewModel = viewmodel

        prefs = IdPreference(applicationContext)
        this.onBackPressedDispatcher.addCallback(this, callback)
        binding.mainNav.selectedItemId = binding.mainNav.menu.getItem(2).itemId
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
            intent.putExtra("logout", true)
            activityResultLauncher.launch(intent)
        } else {
            if (NetworkCheck.checkNetworkState(this)) {
                multipleSearchUser()
            }
        }


        binding.mainNav.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.bottom_main -> {
                    if(mainFrag != null ) {
                        Log.d("added", "added: $added    main clicked")
                        val transaction = supportFragmentManager.beginTransaction()
                        transaction.replace(binding.contentFrame.id, mainFrag!!)
                            .commit()
                        added = true
                    }
                }
                R.id.bottom_rankings -> {
                    rankingFrag = RankingFragment(token)
                    val transaction = supportFragmentManager.beginTransaction()
                    transaction.replace(binding.contentFrame.id, rankingFrag!!)
                        .commit()
                }
            }
            true
        }



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

    }

    override fun onResume() {
        super.onResume()
        state = true
    }

    override fun onRestart() {
        super.onRestart()
        count = 0
        state = true
        finish = false
        Handler(Looper.getMainLooper()).postDelayed({multipleSearchUser()}, 2000)
    }

    override fun onPause() {
        super.onPause()
        state = false
    }

    private fun multipleSearchUser() {
        searchUser()
        Handler(Looper.getMainLooper()).postDelayed({ searchUser() }, 3000)
        Handler(Looper.getMainLooper()).postDelayed({ searchUser() }, 7000)
        Handler(Looper.getMainLooper()).postDelayed({ searchUser() }, 9000)
    }

    /*  메인화면의 유저 정보 검색하기(프로필 사진, 기여도, 랭킹)
        무한히 요청을 보내는 버그 해결
     */
    private fun searchUser() {
//        Toast.makeText(application, "id = $id", Toast.LENGTH_SHORT).show()
        if (token.isNotBlank() && count<7) {
            count++
            val coroutine = CoroutineScope(Dispatchers.Main)
            coroutine.launch {
                if(!this@MainActivity.isFinishing && !finish) {
                    val resultDeferred = coroutine.async(Dispatchers.IO) {
                        viewmodel.getUserInfo(token)
                    }
                    val userInfo = resultDeferred.await()
//                Toast.makeText(applicationContext, "$userInfo", Toast.LENGTH_SHORT).show()
                    if (userInfo.github_id == null) {
                        if (prefs.getRefreshToken("").isBlank()) {
                            if (!this@MainActivity.isFinishing && state) {
                                Log.d("not login", "login activity로 이동")
                                Toast.makeText(applicationContext,"다시 로그인 바랍니다.", Toast.LENGTH_SHORT).show()
                                loginOut = true
                                prefs.setJwtToken("")
                                prefs.setRefreshToken("")
                                prefs.setPostAddress(false)
                                prefs.setWalletAddress("")
                                val intent = Intent(applicationContext, LoginActivity::class.java)
                                intent.putExtra("wallet_address", prefs.getWalletAddress(""))
                                intent.putExtra("token", prefs.getJwtToken(""))
                                intent.putExtra("logout", true)
                                activityResultLauncher.launch(intent)
                            }
                        } else {
                            Log.d("not login", "refreshToken() 호출")
                            refreshToken()
                        }
                    } else {
                        if (userInfo.github_id!!.isNotBlank()) {
                            realModel.github_id = userInfo.github_id
                        }
                        if (userInfo.commits != 0 && userInfo.tier == "SPROUT") {
                            if (prefs.getWalletAddress("") != "") {
                                postWalletAddress(prefs.getWalletAddress(""))
                            }
                            realModel.commits = userInfo.commits
                        } else {
                            realModel.commits = userInfo.commits
                            realModel.tier = userInfo.tier
                        }
                        if (userInfo.token_amount == null) {
                            realModel.token_amount = userInfo.commits
                        } else {
                            realModel.token_amount = userInfo.token_amount
                        }
                        if(userInfo.organization != null) {
                            realModel.organization = userInfo.organization
                        }
                        realModel.profile_image = userInfo.profile_image
                        realModel.rank = userInfo.rank
                        realModel.issues = userInfo.issues
                        realModel.pull_requests = userInfo.pull_requests
                        realModel.reviews = userInfo.reviews
                        if(userInfo.organization_rank !=null) {
                            realModel.organization_rank = userInfo.organization_rank
                        }
                        count = 0
                        if(refreshCount == 0) {
                            refreshCommits()
                            refreshCount++
                        }
                        userInfo.blockchain_url?.let {
                            realModel.blockchain_url = it
                        }
                        realModel.id = userInfo.id
                        realModel.auth_step = userInfo.auth_step
                        realModel.member_github_ids = userInfo.member_github_ids

                        Log.d("token", "token: $token")
                        Log.d("userInfo", "realModel:$realModel")
                        if(realModel.commits != null && realModel.github_id != null && realModel.profile_image != null && realModel.auth_step != null) {
                            Log.d("userInfo", "id:${userInfo.github_id}")
                            mainFrag = MainFragment(token, realModel)
                            refreshMain()
                            if(realModel.tier == "SPROUT" && realModel.token_amount == 0) {
                                finish = true
                            }
                            if(realModel.tier != "SPROUT") {
                                finish = true
                            }
                        } else {
                            refreshToken()
                        }
                    }
                }
            }
        }
    }

    private fun authRequestResult(key: String) {
        val coroutine = CoroutineScope(Dispatchers.Main)
        coroutine.launch {
            if(!this@MainActivity.isFinishing) {
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
    }

    private fun postWalletAddress(address: String) {
//        Toast.makeText(applicationContext, "address: $address", Toast.LENGTH_SHORT).show()
        if (!addressPost && count<7) {
            addressPost = true
            val coroutine = CoroutineScope(Dispatchers.Main)
            coroutine.launch {
                if(!this@MainActivity.isFinishing) {
                    val postwalletDeferred = coroutine.async(Dispatchers.IO) {
                        viewmodel.postWalletAddress(address, prefs.getJwtToken(""))
                    }
                    val postWalletResponse = postwalletDeferred.await()
                    if (postWalletResponse) {
                        refreshCommits()
                    }
                }
            }
        }
    }

    private fun refreshMain() {
        if(realCount == 1) {
            if(mainFrag != null) {
                Log.d("added", "added: $added    refreshMain")
                if(!added) {
                    val transaction = supportFragmentManager.beginTransaction()
                    transaction.add(binding.contentFrame.id, mainFrag!!)
                        .commit()
                    added = true
                } else {
                    val transaction = supportFragmentManager.beginTransaction()
                    transaction.replace(binding.contentFrame.id, mainFrag!!)
                        .commit()
                    added = true
                }
                return
            }
        }
        if(mainFrag != null && binding.mainNav.selectedItemId == binding.mainNav.menu.getItem(2).itemId && state ) {
            Log.d("added", "added: $added    refreshMain")
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(binding.contentFrame.id, mainFrag!!)
                .commit()

            added = true
        }
    }

    private fun refreshToken() {
        val coroutine = CoroutineScope(Dispatchers.Main)
        coroutine.launch {
            if(!this@MainActivity.isFinishing) {
                val refreshDeffered = coroutine.async(Dispatchers.IO) {
                    viewmodel.getNewToken(prefs.getJwtToken(""), prefs.getRefreshToken(""))
                }
                val refresh = refreshDeffered.await()
                if (refresh.refresh_token != null && refresh.access_token != null) {
                    prefs.setJwtToken(refresh.access_token)
                    prefs.setRefreshToken(refresh.refresh_token)
                    token = refresh.access_token
                    Handler(Looper.getMainLooper()).postDelayed({multipleSearchUser()}, 2000)
                    Log.d("refresh success", "token refresh 성공")
                } else {
                    if (!this@MainActivity.isFinishing && state) {
                        Log.d("refresh fail", "token refresh 실패")
                        if(refreshState) {
                            Toast.makeText(applicationContext,"다시 로그인 바랍니다.", Toast.LENGTH_SHORT).show()
                            refreshState = false
                            loginOut = true
                            prefs.setJwtToken("")
                            prefs.setRefreshToken("")
                            prefs.setPostAddress(false)
                            count = 7
                            val intent = Intent(applicationContext, LoginActivity::class.java)
                            intent.putExtra("wallet_address", prefs.getWalletAddress(""))
                            intent.putExtra("token", prefs.getJwtToken(""))
                            intent.putExtra("logout", true)
                            activityResultLauncher.launch(intent)
                        }
                    }
                }
            }
        }
    }

    private fun refreshCommits() {
        if (prefs.getRefreshToken("").isNotBlank()) {
//            Toast.makeText(applicationContext, "refhresh Commits", Toast.LENGTH_SHORT).show()
            Log.d("post", "refresh commits")
            Log.d("refresh 호출", "refresh 호출")
            val coroutine = CoroutineScope(Dispatchers.Main)
            coroutine.launch {
                if(!this@MainActivity.isFinishing) {
                    val refreshDeffered = coroutine.async(Dispatchers.IO) {
                        viewmodel.postCommits(prefs.getJwtToken(""))
                    }
                    val refresh = refreshDeffered.await()
                    Handler(Looper.getMainLooper()).postDelayed({multipleSearchUser()}, 2000)
                }
            }
        }

    }



    //    뒤로가기 1번 누르면 종료 안내 메시지, 2번 누르면 종료
    private val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
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

}
