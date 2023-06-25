package com.dragonguard.android.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.dragonguard.android.R
import com.dragonguard.android.databinding.ActivityTokenHistoryBinding
import com.dragonguard.android.model.klip.TokenHistoryModelItem
import com.dragonguard.android.recycleradapter.TokenListAdapter
import com.dragonguard.android.viewmodel.Viewmodel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okhttp3.internal.notifyAll

class TokenHistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTokenHistoryBinding
    private var token = ""
    var viewmodel = Viewmodel()
    private lateinit var tokenAdapter : TokenListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_token_history)

        setSupportActionBar(binding.toolbar) //커스텀한 toolbar를 액션바로 사용
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)

        token = intent.getStringExtra("token")!!
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
        binding.tokenContributeList.visibility = View.VISIBLE
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home, binding.toolbar.menu)
        return true
    }

    //    뒤로가기, 홈으로 화면전환 기능
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
            R.id.home_menu -> {
                val intent = Intent(applicationContext, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}