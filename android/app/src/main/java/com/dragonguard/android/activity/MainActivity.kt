package com.dragonguard.android.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
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
import com.dragonguard.android.viewmodel.MainViewModel
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {
    companion object {
        lateinit var prefs: IdPreference
    }
    private lateinit var binding: ActivityMainBinding
    private var viewmodel = MainViewModel()
    private var backPressed : Long = 0
    private var id = 0
    //    var count = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.mainViewModel = viewmodel
        prefs = IdPreference(applicationContext)
        id = prefs.getId("id", 0)
        
        if(id == 0){
            registerUser("posite")
        } else {
            searchUser(id)
        }



        //로그인 화면으로 넘어가기
        val intent = Intent(applicationContext, LoginActivity::class.java)
        startActivity(intent)

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

    }

    private fun registerUser(githubId: String) {
        var body = RegisterGithubIdModel(githubId)
        val coroutine = CoroutineScope(Dispatchers.Main)
        coroutine.launch {
            val resultDeferred = coroutine.async(Dispatchers.IO) {
                viewmodel.postRegister(body)
            }
            id = resultDeferred.await()
            delay(500)
            prefs.setId("id", id)
            searchUser(id)
        }

    }

//  메인화면의 유저 정보 검색하기
    private fun searchUser(id: Int){
        val coroutine = CoroutineScope(Dispatchers.Main)
        coroutine.launch {
            val resultDeferred = coroutine.async(Dispatchers.IO) {
                viewmodel.getSearchTierResult(id)
            }
            val userInfo : UserInfoModel = resultDeferred.await()
            if(userInfo.name == null) {
                Toast.makeText(applicationContext, "id 비어있음", Toast.LENGTH_SHORT).show()
                registerUser("posite")
            } else {
                binding.userTier.append(userInfo.tier)
                binding.userToken.append(userInfo.commits.toString())
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
