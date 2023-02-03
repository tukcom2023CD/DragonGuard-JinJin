package com.dragonguard.android.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.dragonguard.android.R
import com.dragonguard.android.databinding.ActivityMainBinding
import com.dragonguard.android.viewmodel.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    var viewmodel = MainViewModel()

    //    var count = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.mainViewModel = viewmodel

        searchTier(1)

        //로그인 화면으로 넘어가기
        val intent = Intent(applicationContext, LoginActivity::class.java)
        startActivity(intent)

/*        화면전환 테스트용 버튼
        binding.btnadd.setOnClickListener {
            count++
            binding.btnadd.setText(count.toString())
        }

 */
        binding.lookRanking.setOnClickListener {
            val intent = Intent(applicationContext, RankingsActivity::class.java)
            startActivity(intent)
        }

        viewmodel.onUserIconSelected.observe(this, Observer {
            if(viewmodel.onUserIconSelected.value == true){
                val intent = Intent(applicationContext, MenuActivity::class.java)
                startActivity(intent)
            }
        })

        viewmodel.onSearchClickListener.observe(this, Observer {
            if(viewmodel.onSearchClickListener.value == true) {
                val intent = Intent(applicationContext, SearchActivity::class.java)
                startActivity(intent)
            }
        })

    }

    private fun searchTier(id: Int){
        var tier = ""
        val coroutine = CoroutineScope(Dispatchers.Main)
        coroutine.launch {
            val resultDeferred = coroutine.async(Dispatchers.IO) {
                viewmodel.getSearchTierResult(1)
            }
            tier = resultDeferred.await()
            Log.d("api 시도", "api result에 넣기 $tier")
            binding.userTier.append(tier)
        }
    }

    private fun closeKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.searchName.windowToken, 0)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        closeKeyboard()
        return super.dispatchTouchEvent(ev)
    }

}
