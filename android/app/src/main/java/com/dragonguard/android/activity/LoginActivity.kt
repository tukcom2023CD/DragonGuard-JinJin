package com.dragonguard.android.activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import com.dragonguard.android.R
import com.dragonguard.android.connect.NetworkCheck
import com.dragonguard.android.databinding.ActivityLoginBinding
import com.dragonguard.android.model.Bapp
import com.dragonguard.android.viewmodel.Viewmodel
import com.dragonguard.android.model.WalletAuthRequestModel
import com.dragonguard.android.preferences.IdPreference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private var backPressed : Long = 0
    private lateinit var binding :ActivityLoginBinding
    private var viewmodel = Viewmodel()
    private val body = WalletAuthRequestModel(Bapp("GitRank"), "auth")
    private var walletAddress = ""
    private var key = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.loginViewmodel = viewmodel

        val intent = getIntent()
        walletAddress = intent.getStringExtra("wallet_address")!!
        if(walletAddress.isNotBlank()) {
            val intent = Intent(applicationContext, MainActivity::class.java)
            setResult(1, intent)
            val handler = Handler(Looper.getMainLooper()).postDelayed({finish()},500)
        }
        binding.walletAuth.setOnClickListener{
            if(NetworkCheck.checkNetworkState(this)) {
                walletAuthRequest()
            }
        }
        binding.walletFinish.setOnClickListener {
            if(key.isNotBlank()) {
                val intent = Intent(applicationContext, MainActivity::class.java)
                intent.putExtra("key", key)
                setResult(0, intent)
                finish()
            } else {
                Toast.makeText(applicationContext, "wallet 인증 후 완료를 눌러주세요!!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun walletAuthRequest() {
        val coroutine = CoroutineScope(Dispatchers.Main)
        coroutine.launch {
            val authResponseDeffered = coroutine.async(Dispatchers.IO) {
                viewmodel.postWalletAuth(body)
            }
            val authResponse = authResponseDeffered.await()
            if(authResponse.request_key.isNullOrEmpty() || authResponse.status != "prepared" ) {
                val handler = Handler(Looper.getMainLooper())
                handler.postDelayed({walletAuthRequest()}, 1000)
            } else {
                key = authResponse.request_key
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://klipwallet.com/?target=/a2a?request_key=${authResponse.request_key}"))
                startActivity(intent)
            }
        }
    }
//    뒤로가기 1번 누르면 종료 안내 메시지, 2번 누르면 종료
    override fun onBackPressed() {
//        super.onBackPressed()
        if(System.currentTimeMillis() > backPressed + 2500) {
            backPressed = System.currentTimeMillis()
            Toast.makeText(applicationContext, "Back 버튼을 한번 더 누르면 종료합니다.", Toast.LENGTH_SHORT).show()
            return
        }

        if(System.currentTimeMillis() <= backPressed + 2500) {
            finishAffinity()
        }
    }
}