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
import com.dragonguard.android.R
import com.dragonguard.android.activity.compare.RepoChooseActivity
import com.dragonguard.android.activity.menu.MenuActivity
import com.dragonguard.android.activity.ranking.RankingsActivity
import com.dragonguard.android.activity.search.SearchActivity
import com.dragonguard.android.connect.NetworkCheck
import com.dragonguard.android.databinding.ActivityMainBinding
import com.dragonguard.android.model.RegisterGithubIdModel
import com.dragonguard.android.model.UserInfoModel
import com.dragonguard.android.preferences.IdPreference
import com.dragonguard.android.viewmodel.Viewmodel
import kotlinx.coroutines.*
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
                val githubId = walletIntent.getStringExtra("githubId")
//            Toast.makeText(applicationContext, requestKey, Toast.LENGTH_SHORT).show()
                if(!NetworkCheck.checkNetworkState(this)) {
                    Toast.makeText(applicationContext, "인터넷을 연결하세요!!", Toast.LENGTH_LONG).show()
                } else {
                    prefs.setGithubId("githubId", githubId!!)
                    registerUser(githubId)
                    authRequestResult(requestKey!!)
                }
            } catch(e: Exception) {
//                finishAffinity()
//                val intent = Intent(this, MainActivity::class.java)
//                startActivity(intent)
//                exitProcess(0)
            }

        } else if(it.resultCode == 1) {
//            postWalletAddress(userId, prefs.getWalletAddress("wallet_address", ""))
//            Toast.makeText(applicationContext, "skip 주소 : $walletAddress", Toast.LENGTH_SHORT).show()
        }
    }
    companion object {
        lateinit var prefs: IdPreference
    }
    private lateinit var binding: ActivityMainBinding
    private var viewmodel = Viewmodel()
    private var backPressed : Long = 0
    private var userId = 0
    private var githubId = ""
    private var walletAddress = ""
    private var registered = false
    private var address = false
    //    var count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.mainViewModel = viewmodel
        val logout = intent.getBooleanExtra("logout", false)
        prefs = IdPreference(applicationContext)
        userId = prefs.getId("id", 0)
        githubId = prefs.getGithubId("githubId", "")
        val result = intent?.data?.getQueryParameter("code")
        if(result != null) {
            val coroutine = CoroutineScope(Dispatchers.IO)
            coroutine.launch {
                val deffered = coroutine.async ( Dispatchers.IO ) {
                    viewmodel.getOauthToken(result)
                }
                val resultToken = deffered.await()
                if(resultToken.access_token == null) {
                    Log.d("intent github", "실패!!")
                } else {
                    Log.d("intent github", "성공!! ${resultToken.access_token}, ${resultToken.scope}, ${resultToken.token_type}")
                    walletAddress = prefs.getWalletAddress("wallet_address", "")
//        Toast.makeText(applicationContext, walletAddress, Toast.LENGTH_SHORT).show()
                    val intent = Intent(applicationContext, LoginActivity::class.java)
                    intent.putExtra("access_token", resultToken.access_token)
                    intent.putExtra("wallet_address", walletAddress)
                    activityResultLauncher.launch(intent)
                }
            }
        } else {
            if(logout) {
                prefs.setWalletAddress("wallet_address", "")
                prefs.setGithubId("githubId", "")
                prefs.setId("id", 0)
            }
            walletAddress = prefs.getWalletAddress("wallet_address", "")
//        Toast.makeText(applicationContext, walletAddress, Toast.LENGTH_SHORT).show()
            val intent = Intent(applicationContext, LoginActivity::class.java)
            intent.putExtra("wallet_address", walletAddress)
            intent.putExtra("logout", logout)
            activityResultLauncher.launch(intent)
        }

        //로그인 화면으로 넘어가기

        if(userId == 0){
            if(NetworkCheck.checkNetworkState(this)) {
                registerUser(githubId)
            }
        } else {
            if(NetworkCheck.checkNetworkState(this)) {
                searchUser(userId)
            }
        }

//        랭킹 보러가기 버튼 눌렀을 때 랭킹 화면으로 전환
        binding.lookRanking.setOnClickListener {
            val intent = Intent(applicationContext, RankingsActivity::class.java)
            startActivity(intent)
        }

//        유저 아이디, 프로필을 눌렀을 때 메뉴 화면으로 전환
        viewmodel.onUserIconSelected.observe(this, Observer {
            if(viewmodel.onUserIconSelected.value == true){
                val intent = Intent(applicationContext, MenuActivity::class.java)
                startActivity(intent)
            }
        })

//        검색창, 검색 아이콘 눌렀을 때 검색화면으로 전환
        viewmodel.onSearchClickListener.observe(this, Observer {
            if(viewmodel.onSearchClickListener.value == true) {
                val intent = Intent(applicationContext, SearchActivity::class.java)
                startActivity(intent)
            }
        })

        binding.repoCompare.setOnClickListener {
            val intent = Intent(applicationContext, RepoChooseActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        if(userId != 0) {
            searchUser(userId)
            val handler = Handler(Looper.getMainLooper())
            handler.postDelayed({searchUser(userId)}, 2000)
            handler.postDelayed({searchUser(userId)}, 4000)
        }
    }

//    등록되어있지 않을 경우 post 요청을 통해 가입하기
    private fun registerUser(githubId: String) {
        var body = RegisterGithubIdModel(githubId)
        val coroutine = CoroutineScope(Dispatchers.Main)
        coroutine.launch {
            val resultDeferred = coroutine.async(Dispatchers.IO) {
                viewmodel.postRegister(body)
            }
            userId = resultDeferred.await()
            if(userId != 0) {
                prefs.setId("id", userId)
//            Toast.makeText(application, "id = $userId", Toast.LENGTH_SHORT).show()
                searchUser(userId)
            }
        }

    }

/*  메인화면의 유저 정보 검색하기(프로필 사진, 기여도, 랭킹)
    무한히 요청을 보내는 버그 해결
 */
    private fun searchUser(id: Int){
//        Toast.makeText(application, "id = $id", Toast.LENGTH_SHORT).show()
        val coroutine = CoroutineScope(Dispatchers.Main)
        coroutine.launch {
            val resultDeferred = coroutine.async(Dispatchers.IO) {
                viewmodel.getSearchTierResult(id)
            }
            val userInfo : UserInfoModel = resultDeferred.await()
            if(userInfo.githubId == null || userInfo.id == null || userInfo.rank == null || userInfo.commits ==null) {
//                Toast.makeText(applicationContext, "id 비어있음", Toast.LENGTH_SHORT).show()
                if(!registered) {
                    registered = true
                    val handler = Handler(Looper.getMainLooper())
                    handler.postDelayed({registerUser(prefs.getGithubId("githubId", ""))}, 2000)
                }
            } else {
                if(userInfo.githubId.isNotBlank()) {
                    binding.userId.text = userInfo.githubId
                }
                if(userInfo.commits != 0 && userInfo.tier == "SPROUT" && !address) {
                    if(prefs.getWalletAddress("wallet_address", "") != "") {
                        address = true
                        postWalletAddress(userId, prefs.getWalletAddress("wallet_address", ""))
                    } else {
                        binding.userTier.text = "내 티어 : ${userInfo.tier}"
                    }
                    val handler = Handler(Looper.getMainLooper())
                    handler.postDelayed({searchUser(userId)},2000)
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

    private fun authRequestResult(key: String) {
        val coroutine = CoroutineScope(Dispatchers.Main)
        coroutine.launch {
            val authResponseDeferred = coroutine.async(Dispatchers.IO) {
                viewmodel.getWalletAuthResult(key)
            }
            val authResponse = authResponseDeferred.await()
            if(authResponse.request_key.isNullOrEmpty() || authResponse.status != "completed" || authResponse.result == null || prefs.getGithubId("githubId", "")=="") {
//                Toast.makeText(applicationContext, "auth 결과 : 재전송", Toast.LENGTH_SHORT).show()
                val intent = Intent(applicationContext, LoginActivity::class.java)
                intent.putExtra("wallet_address", walletAddress)
                activityResultLauncher.launch(intent)

            } else {
//                Toast.makeText(applicationContext, "wallet 주소 : ${authResponse.result.klaytn_address}", Toast.LENGTH_SHORT).show()
                prefs.setWalletAddress("wallet_address", authResponse.result.klaytn_address)
            }
        }
    }

    private fun postWalletAddress(id: Int, address: String) {
        val coroutine = CoroutineScope(Dispatchers.Main)
        coroutine.launch {
            val postwalletDeferred = coroutine.async(Dispatchers.IO) {
                viewmodel.postWalletAddress(id, address)
            }
            val postWalletResponse = postwalletDeferred.await()
//            if(!postWalletResponse) {
//                postWalletAddress(userId, address)
//            } else {
////                Toast.makeText(applicationContext, "wallet post : 성공!", Toast.LENGTH_SHORT).show()
//            }
        }
    }

    private fun userInfoTimer() {
        Toast.makeText(applicationContext,"timer", Toast.LENGTH_SHORT).show()
        Timer().scheduleAtFixedRate(3000,2000){
            if(githubId != "") {
                if(NetworkCheck.checkNetworkState(applicationContext)) {
                    Log.d("info", "github id : $githubId, klip address : $walletAddress")
                    searchUser(userId)
                }
            }
//            Toast.makeText(applicationContext, "반복", Toast.LENGTH_SHORT).show()
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
