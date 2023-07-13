package com.dragonguard.android.activity.basic

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.dragonguard.android.R
import com.dragonguard.android.databinding.ActivityTokenHistoryBinding
import com.dragonguard.android.model.klip.TokenHistoryModelItem
import com.dragonguard.android.adapters.TokenListAdapter
import com.dragonguard.android.viewmodel.Viewmodel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class TokenHistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTokenHistoryBinding
    private var token = ""
    var viewmodel = Viewmodel()
    private lateinit var tokenAdapter : TokenListAdapter
    private var refresh = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_token_history)

        setSupportActionBar(binding.toolbar) //커스텀한 toolbar를 액션바로 사용
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.back)

        token = intent.getStringExtra("token")!!
        callTokenHistory()
    }

    private fun callTokenHistory() {
        val coroutine = CoroutineScope(Dispatchers.Main)
        coroutine.launch {
            val resultDeferred = coroutine.async(Dispatchers.IO) {
                viewmodel.getTokenHistory(token)
            }
            val result = resultDeferred.await()
            result?.let { it->
                initRecycler(it)
            }
        }
    }

    private fun initRecycler(result: ArrayList<TokenHistoryModelItem>) {
        tokenAdapter =TokenListAdapter(result, this@TokenHistoryActivity)
        binding.tokenContributeList.adapter = tokenAdapter
        binding.tokenContributeList.layoutManager = LinearLayoutManager(this)
        tokenAdapter.notifyDataSetChanged()

        Handler(Looper.getMainLooper()).postDelayed({binding.tokenContributeList.visibility = View.VISIBLE}, 500)
    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.refresh, binding.toolbar.menu)
//        return true
//    }
    

    //    뒤로가기, 홈으로 화면전환 기능
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
            }

//            R.id.refresh_button -> {
//                if(refresh) {
//                    refresh = false
//                    callTokenHistory()
//                }
//            }
        }
        return super.onOptionsItemSelected(item)
    }
}