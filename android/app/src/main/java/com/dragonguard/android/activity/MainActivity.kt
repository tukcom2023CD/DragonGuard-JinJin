package com.dragonguard.android.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.dragonguard.android.R
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
    companion object {
        lateinit var prefs: IdPreference
    }
    private lateinit var binding: ActivityMainBinding
    private var viewmodel = Viewmodel()
    private var backPressed : Long = 0
    private var userId = 0
    //    var count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.mainViewModel = viewmodel
        prefs = IdPreference(applicationContext)
        userId = prefs.getId("id", 0)

        if(userId == 0){
            registerUser("posite")
        } else {
            searchUser(userId)
        }

        //로그인 화면으로 넘어가기
        val intent = Intent(applicationContext, LoginActivity::class.java)
        startActivity(intent)
        Timer().scheduleAtFixedRate(2000,2000){
//            Toast.makeText(applicationContext, "반복", Toast.LENGTH_SHORT).show()
            searchUser(userId)
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
//            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://klipwallet.com/?target=/a2a?request_key=9de032df-1e4c-4411-8ca7-5101908f0e32"))
//            startActivity(intent)
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
            prefs.setId("id", userId)
//            Toast.makeText(application, "id = $userId", Toast.LENGTH_SHORT).show()
            searchUser(userId)
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
            if(userInfo.githubId == null || userInfo.id == null || userInfo.rank == null || userInfo.commits ==null || userInfo.tier == null) {
//                Toast.makeText(applicationContext, "id 비어있음", Toast.LENGTH_SHORT).show()
                val handler = Handler()
                handler.postDelayed({registerUser("posite")}, 500)
            } else {
                binding.userTier.text = "내 티어 : ${userInfo.tier}"
                binding.userToken.text = "내 기여도 : ${userInfo.commits}"
                binding.userRanking.text = userInfo.rank
                Glide.with(binding.githubProfile).load(userInfo.profileImage).into(binding.githubProfile)

            }
        }
    }


    //    edittext 키보드 제거
    private fun closeKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.searchName.windowToken, 0)
    }

//    화면을 눌렀을때 키보드 제거
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        closeKeyboard()
        return super.dispatchTouchEvent(ev)
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
