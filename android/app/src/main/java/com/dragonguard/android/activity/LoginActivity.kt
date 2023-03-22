package com.dragonguard.android.activity

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.webkit.*
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import com.dragonguard.android.BuildConfig
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
import java.net.CookieHandler

class LoginActivity : AppCompatActivity() {
    companion object {
        lateinit var prefs: IdPreference
    }
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
        Log.d("login", "loginactivity1")
        prefs = IdPreference(applicationContext)
        //쿠키 확인 코드
//        val cookieManager = CookieManager()
//        CookieHandler.setDefault(cookieManager)
//        val cookieList = cookieManager.cookieStore.cookies
//        for (cookie in cookieList) {
//            Log.d("cookie", "cookies :  ${cookie.name} = ${cookie.value}")
//        }
        val intent = intent
        val token = intent.getStringExtra("token")
        val logout = intent.getBooleanExtra("logout", false)
        if(token != "") {
//            Toast.makeText(applicationContext, "jwt token : $token", Toast.LENGTH_SHORT).show()
            binding.githubAuth.isEnabled = false
            binding.githubAuth.setTextColor(Color.BLACK)
        } else {
            binding.githubAuth.isEnabled = true
        }
        if(logout) {
            prefs.setKey("key", "")
        }

        val address = intent.getStringExtra("wallet_address")
        address?.let{
            walletAddress = address
            if(walletAddress.isNotBlank() && !token.isNullOrEmpty()) {
//                Log.d("wallet", "지갑주소 이미 있음 $walletAddress")
//                Toast.makeText(applicationContext, "wallet : $walletAddress", Toast.LENGTH_SHORT).show()
                val intentW = Intent(applicationContext, MainActivity::class.java)
                setResult(1, intentW)
                finish()
            }
        }


        key = prefs.getKey("key", "")
        binding.oauthWebView.apply {
            settings.javaScriptEnabled = true // 자바스크립트 허용
            settings.javaScriptCanOpenWindowsAutomatically = false
            // 팝업창을 띄울 경우가 있는데, 해당 속성을 추가해야 window.open() 이 제대로 작동 , 자바스크립트 새창도 띄우기 허용여부
            settings.setSupportMultipleWindows(false) // 새창 띄우기 허용 여부 (멀티뷰)
            settings.loadsImagesAutomatically = true // 웹뷰가 앱에 등록되어 있는 이미지 리소스를 자동으로 로드하도록 설정하는 속성
            settings.useWideViewPort = true // 화면 사이즈 맞추기 허용 여부
            settings.loadWithOverviewMode = true // 메타태그 허용 여부
            settings.setSupportZoom(true) // 화면 줌 허용여부
            settings.builtInZoomControls = false // 화면 확대 축소 허용여부
            settings.displayZoomControls = false // 줌 컨트롤 없애기.
            settings.cacheMode = WebSettings.LOAD_NO_CACHE
            settings.domStorageEnabled = true // 로컬 스토리지 사용 여부를 설정하는 속성으로 팝업창등을 '하루동안 보지 않기' 기능 사용에 필요
            settings.allowContentAccess // 웹뷰 내에서 파일 액세스 활성화 여부
            settings.userAgentString = "app" // 웹에서 해당 속성을 통해 앱에서 띄운 웹뷰로 인지 할 수 있도록 합니다.
            settings.defaultTextEncodingName = "UTF-8" // 인코딩 설정
            settings.databaseEnabled = true //Database Storage API 사용 여부 설정
        }
        binding.oauthWebView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                if (request.url.toString().startsWith("gitrank://github-auth")) {
                    // Redirect to your app
//                    val cookies = CookieManager.getInstance().getCookie(request.url.toString())
//                    Log.d("cookie", "name: $cookies")
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(request.url.toString()))
//                    intent.putExtra("cookie", cookies)
                    startActivity(intent)
                    return true
                }
                return super.shouldOverrideUrlLoading(view, request)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
            }

        }
        binding.githubAuth.setOnClickListener {
            binding.loginMain.visibility = View.GONE
            binding.loginGithub.visibility = View.VISIBLE
            binding.oauthWebView.isEnabled = true
            binding.oauthWebView.loadUrl("${BuildConfig.api}oauth2/authorize/github")
//            val intentAuth = Intent(Intent.ACTION_VIEW, Uri.parse("${BuildConfig.api}oauth2/authorize/github"))
//            startActivity(intentAuth)

//            val coroutine = CoroutineScope(Dispatchers.Main)
//            coroutine.launch {
//                val deffered = coroutine.async(Dispatchers.IO){
//                    viewmodel.getGithubLogin()
//                }
//                val result = deffered.await()
//            }
        }
        binding.walletAuth.setOnClickListener{
            if(NetworkCheck.checkNetworkState(this)) {
                walletAuthRequest()
            }
        }
        binding.walletFinish.setOnClickListener {
            if(key.isNotBlank()) {
                val intent = Intent(applicationContext, MainActivity::class.java)
                Log.d("info", "key : $key")
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
                prefs.setKey("key", authResponse.request_key)
                Log.d("key", "key : ${authResponse.request_key}")
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
            binding.loginGithub.visibility = View.GONE
            binding.loginMain.visibility = View.VISIBLE
            binding.oauthWebView.isEnabled = false
            Toast.makeText(applicationContext, "Back 버튼을 한번 더 누르면 종료합니다.", Toast.LENGTH_SHORT).show()
            return
        }

        if(System.currentTimeMillis() <= backPressed + 2500) {
            finishAffinity()
        }
    }

}