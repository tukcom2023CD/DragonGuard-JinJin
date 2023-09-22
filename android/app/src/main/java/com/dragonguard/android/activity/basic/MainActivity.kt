package com.dragonguard.android.activity.basic

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
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
import com.dragonguard.android.fragment.ClientProfileFragment
import com.dragonguard.android.fragment.CompareSearchFragment
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
                Log.d("지갑주소 전송", "전송전 0")
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
                Log.d("지갑주소 전송", "전송전 1")
                val tokenIntent = it.data
                val realToken = tokenIntent!!.getStringExtra("token")
                val refreshToken = tokenIntent!!.getStringExtra("refresh")
                token = realToken!!
                prefs.setRefreshToken(refreshToken!!)
                prefs.setJwtToken(realToken)
                Log.d("refreshToken", "refresh : ${prefs.getRefreshToken("")}")
                multipleSearchUser()
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
    private var compareFrag: CompareSearchFragment? = null
    private var profileFrag: ClientProfileFragment? = null
    private var imgRefresh = true
    private var added = false
    private var realModel = UserInfoModel(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null)
    private var finish = false
    private var post = true

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        count = 0
        finish = false
        Log.d("on", "onnewintent")
        val logout = intent?.getBooleanExtra("logout", false)
        if(token == "") {
            val intent = Intent(applicationContext, LoginActivity::class.java)
            intent.putExtra("key", prefs.getKey(""))
            activityResultLauncher.launch(intent)
        }
        if (logout != null) {
            if(!this@MainActivity.isFinishing) {
                loginOut = logout
                if (loginOut) {
                    binding.mainNav.selectedItemId = binding.mainNav.menu.getItem(0).itemId
                    prefs.setWalletAddress("")
                    loginOut = true
                    prefs.setJwtToken("")
                    prefs.setRefreshToken("")
                    prefs.setPostAddress(false)
                    val transaction = supportFragmentManager.beginTransaction()
                    supportFragmentManager.fragments.forEach {
                        transaction.remove(it)
                    }
                    transaction.commit()
                    mainFrag?.let{
                        it.clearView()
                    }
                    mainFrag = null
                    compareFrag = null
                    profileFrag = null
                    rankingFrag = null
                    Log.d("로그인 필요", "로그인 필요")
                    val intent = Intent(applicationContext, LoginActivity::class.java)
                    intent.putExtra("wallet_address", prefs.getWalletAddress(""))
                    intent.putExtra("token", prefs.getJwtToken(""))
                    intent.putExtra("logout", true)
                    activityResultLauncher.launch(intent)
                } else {
                    postCommits()
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
        binding.mainNav.selectedItemId = binding.mainNav.menu.getItem(0).itemId
        binding.mainLoading.resumeAnimation()
        binding.mainLoading.visibility = View.VISIBLE
        if (loginOut) {
            prefs.setWalletAddress("")
        }
        val intent = intent
        val key = intent.getStringExtra("key")
        val refresh = intent.getStringExtra("refresh")
        val access = intent.getStringExtra("access")
        Log.d("tokenMain", "access : $access")
        if (access != null) {
            token = access
            prefs.setJwtToken(access)
        }
        if (refresh != null) {
            prefs.setRefreshToken(refresh)
        }
        if(!key.isNullOrBlank() ) {
            Log.d("key", "key: $key")
            prefs.setKey(key)
            authRequestResult(prefs.getKey(""))
        }
        CoroutineScope(Dispatchers.IO).launch {
            delay(2000)
            val currentState = checkState(token)
            Log.d("로그인 상태 main", currentState.toString())
            if (currentState) {
                if (NetworkCheck.checkNetworkState(this@MainActivity)) {
                    withContext(Dispatchers.Main) {
                        refreshCommits()
                    }
                }

            } else {
                Log.d("token", "token: $token  wallet: ${prefs.getWalletAddress("")}")
                Log.d("로그인 필요", "로그인 필요")
                val intent = Intent(applicationContext, LoginActivity::class.java)
//            intent.putExtra("wallet_address", prefs.getWalletAddress(""))
//            intent.putExtra("token", prefs.getJwtToken(""))
//            intent.putExtra("refresh", prefs.getRefreshToken(""))
                intent.putExtra("logout", true)
                withContext(Dispatchers.Main) {
                    activityResultLauncher.launch(intent)
                }
            }
        }




        binding.mainNav.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.bottom_main -> {
                    if(mainFrag != null ) {
                        val main = MainFragment(token, realModel, true)
                        Log.d("added", "added: $added    main clicked")
                        val transaction = supportFragmentManager.beginTransaction()
                        transaction.replace(binding.contentFrame.id, main)
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
                R.id.bottom_compare -> {
                    compareFrag = CompareSearchFragment(token)
                    val transaction = supportFragmentManager.beginTransaction()
                    transaction.replace(binding.contentFrame.id, compareFrag!!)
                        .commit()
                }
                R.id.bottom_profile -> {
                    Log.d("user name", "user name: ${realModel.github_id}")
                    realModel.github_id?.let {
                        profileFrag = ClientProfileFragment(token, viewmodel, it)
                        val transaction = supportFragmentManager.beginTransaction()
                        transaction.replace(binding.contentFrame.id, profileFrag!!)
                            .commit()
                    }
                }
//                R.id.bottom_questions -> {
//                    logOut()
//                }
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
        count = 0
        state = true
    }

    override fun onRestart() {
        super.onRestart()
        state = true
        finish = false
        loginOut = false
    }

    override fun onPause() {
        super.onPause()
        state = false
    }

    private fun multipleSearchUser() {
        Handler(Looper.getMainLooper()).postDelayed({ searchUser() }, 3500)
        Handler(Looper.getMainLooper()).postDelayed({ searchUser() }, 6500)
        Handler(Looper.getMainLooper()).postDelayed({ searchUser() }, 9500)
        Handler(Looper.getMainLooper()).postDelayed({ searchUser() }, 12500)
    }

    /*  메인화면의 유저 정보 검색하기(프로필 사진, 기여도, 랭킹)
        무한히 요청을 보내는 버그 해결
     */
    private fun searchUser() {
//        Toast.makeText(application, "id = $id", Toast.LENGTH_SHORT).show()
        Log.d("search user","logout: $loginOut, state: $state")
        if (token.isNotBlank() && count<8 && !loginOut && state) {
            count++
            val coroutine = CoroutineScope(Dispatchers.Main)
            coroutine.launch {
                if(!this@MainActivity.isFinishing && !finish) {
                    val resultDeferred = coroutine.async(Dispatchers.IO) {
                        viewmodel.getUserInfo(token)
                    }
                    val userInfo = resultDeferred.await()
                    checkUserInfo(userInfo)
//                Toast.makeText(applicationContext, "$userInfo", Toast.LENGTH_SHORT).show()
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
        if (!addressPost && count<8) {
            addressPost = true
            val coroutine = CoroutineScope(Dispatchers.Main)
            coroutine.launch {
                if(!this@MainActivity.isFinishing) {
                    val postwalletDeferred = coroutine.async(Dispatchers.IO) {
                        viewmodel.postWalletAddress(address, prefs.getJwtToken(""))
                    }
                    val postWalletResponse = postwalletDeferred.await()
                    Log.d("지갑주소", "지갑주소 전송 $postWalletResponse")
                    if (postWalletResponse) {
                        refreshCommits()
                    }
                }
            }
        }
    }

    private fun logOut() {
        if(!this@MainActivity.isFinishing) {
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
    private fun refreshMain() {
        binding.mainLoading.pauseAnimation()
        binding.mainLoading.visibility = View.GONE
        binding.mainNav.visibility = View.VISIBLE
        val main = MainFragment(token, realModel, true)
        if(realCount >= 1) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(binding.contentFrame.id, main)
                .commit()
            added = true
            return
        }
        if(mainFrag != null && binding.mainNav.selectedItemId == binding.mainNav.menu.getItem(0).itemId && state ) {
            Log.d("added", "added: $added    refreshMain")
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(binding.contentFrame.id, main)
                .commit()
            added = true
        }
    }

    private fun checkUserInfo(userInfo: UserInfoModel) {
        if (userInfo.github_id == null && !loginOut) {
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
            userInfo.blockchain_url?.let {
                realModel.blockchain_url = it
            }
            realModel.id = userInfo.id
            realModel.auth_step = userInfo.auth_step
            realModel.member_github_ids = userInfo.member_github_ids
            realModel.is_last = userInfo.is_last
            realModel.name = userInfo.name

            Log.d("token", "token: $token")
            Log.d("userInfo", "realModel:$realModel")
            if(realModel.commits != null && realModel.github_id != null && realModel.profile_image != null && realModel.auth_step != null) {
                Log.d("userInfo", "id:${userInfo.github_id}")
                mainFrag = MainFragment(token, realModel, imgRefresh)
//                binding.mainLoading.pauseAnimation()
//                binding.mainLoading.visibility = View.GONE
//                binding.mainNav.visibility = View.VISIBLE
                Log.d("메인", "메인화면 초기화")
                imgRefresh = false
                var sum = 0
                realModel.commits?.let {
                    sum += it
                }
                realModel.issues?.let {
                    sum += it
                }
                realModel.pull_requests?.let {
                    sum += it
                }
                realModel.reviews?.let {
                    sum += it
                }
                Log.d("sum", "sum : $sum  amount: ${realModel.token_amount}")
                if(sum != 0 && realModel.token_amount != sum && post) {
                    post = false
                    postCommits()
                }
                refreshMain()
//                if(realModel.tier != "SPROUT") {
//                    finish = true
//                }
            } else {
                if(refreshCount == 0) {
                    refreshCount++
                    refreshCommits()
                }
            }
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
        refreshCount++
        if (prefs.getRefreshToken("").isNotBlank()) {
//            Toast.makeText(applicationContext, "refhresh Commits", Toast.LENGTH_SHORT).show()
            Log.d("post", "refresh commits")
            Log.d("refresh 호출", "refresh 호출")
            val coroutine = CoroutineScope(Dispatchers.Main)
            coroutine.launch {
                if(!this@MainActivity.isFinishing) {
                    val refreshDeffered = coroutine.async(Dispatchers.IO) {
                        viewmodel.updateUserInfo(prefs.getJwtToken(""))
                    }
                    val refreshResult = refreshDeffered.await()
                    checkUserInfo(refreshResult)
                }
            }
        }

    }

    private fun postCommits() {
        val coroutine2 = CoroutineScope(Dispatchers.Main)
        coroutine2.launch {
            Log.d("post", "post commit")
            if(!this@MainActivity.isFinishing) {
                val refreshDeffered = coroutine2.async(Dispatchers.IO) {
                    viewmodel.postCommits(prefs.getJwtToken(""))
                }
                val refreshResult = refreshDeffered.await()
                Log.d("post", "post token : $token")
                Handler(Looper.getMainLooper()).postDelayed({searchUser()}, 2000)
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

    fun getNavSize(): Int {
        return binding.mainNav.height
    }

    private suspend fun checkState(token: String): Boolean {
        Log.d("로그인 상태 main", "로그인 토큰 : $token")
        var result: Boolean? = false
        val coroutine = CoroutineScope(Dispatchers.Main)
        coroutine.async {
            if (!this@MainActivity.isFinishing) {
                val resultDeffered = coroutine.async(Dispatchers.IO) {
                    viewmodel.checkLoginState(token)
                }

                result = resultDeffered.await()
                Log.d("로그인 상태 main", "로그인 상태 결과 : $result")
            }

        }.await()
        return if(result == null) {
            false
        } else {
            result!!
        }
    }



}
