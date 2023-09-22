package com.dragonguard.android.activity.basic

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.*
import android.util.Log
import android.view.View
import android.webkit.*
import android.webkit.WebView.WebViewTransport
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsCallback
import androidx.browser.customtabs.CustomTabsClient
import androidx.browser.customtabs.CustomTabsIntent
import androidx.browser.customtabs.CustomTabsService
import androidx.browser.customtabs.CustomTabsServiceConnection
import androidx.browser.customtabs.CustomTabsSession
import androidx.databinding.DataBindingUtil
import com.dragonguard.android.BuildConfig
import com.dragonguard.android.R
import com.dragonguard.android.connect.NetworkCheck
import com.dragonguard.android.databinding.ActivityLoginBinding
import com.dragonguard.android.model.klip.Bapp
import com.dragonguard.android.model.klip.CallBack
import com.dragonguard.android.model.klip.WalletAuthRequestModel
import com.dragonguard.android.preferences.IdPreference
import com.dragonguard.android.viewmodel.Viewmodel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class LoginActivity : AppCompatActivity() {
    companion object {
        lateinit var prefs: IdPreference
    }

    private var backPressed: Long = 0
    private lateinit var binding: ActivityLoginBinding
    private var viewmodel = Viewmodel()
    private val body = WalletAuthRequestModel(Bapp("GitRank", CallBack()), "auth")
    private var walletAddress = ""
    private var key = ""
    private val oauthUrl = "${BuildConfig.api}oauth2/authorize/github"
    private lateinit var mClient: CustomTabsClient
    private val appUrl = "gitrank://github-auth"

    override fun onCreate(savedInstanceState: Bundle?) {
        CookieManager.getInstance().setAcceptCookie(true)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.loginViewmodel = viewmodel
        Log.d("시작", "loginactivity1")
        prefs = IdPreference(applicationContext)
        //쿠키 확인 코드
        this.onBackPressedDispatcher.addCallback(this, callback)
        val defaultBrowser = getDefaultBrowserPackageName(this@LoginActivity)
        if(defaultBrowser != null) {
            CustomTabsClient.bindCustomTabsService(this@LoginActivity, defaultBrowser, connection)
        } else {
            CustomTabsClient.bindCustomTabsService(this@LoginActivity, "com.android.chrome", connection)
        }


        val intent = intent
        var token = intent.getStringExtra("token")
        var refresh = intent.getStringExtra("refresh")
        intent.getStringExtra("key")?.let {
            key = it
        }
        Log.d("info", "로그인 화면 token: $token")
        Log.d("info", "로그인 화면 refresh: $refresh")
        val logout = intent.getBooleanExtra("logout", false)
        if (prefs.getJwtToken("").isNotBlank() && prefs.getRefreshToken("").isNotBlank()) {
//            Toast.makeText(applicationContext, "jwt token : $token", Toast.LENGTH_SHORT).show()
            if(prefs.getKey("").isNotBlank()){
                val intentF = Intent(applicationContext, MainActivity::class.java)
                Log.d("info", "key : $key")
                intentF.putExtra("key", key)
                intentF.putExtra("access", token)
                intentF.putExtra("refresh", refresh)
                startActivity(intentF)
                finish()
            } else {
                binding.githubAuth.isEnabled = false
                binding.githubAuth.setTextColor(Color.BLACK)
                checkState(prefs.getJwtToken(""), prefs.getRefreshToken(""))
            }
        } else {
            binding.githubAuth.isEnabled = true
            binding.walletAuth.isEnabled = false
//            binding.walletFinish.isEnabled = false
        }
        if (logout) {
            prefs.setKey("")
            prefs.setJwtToken("")
            prefs.setRefreshToken("")
            binding.oauthWebView.apply {
                clearHistory()
                clearCache(true)
            }
            val cookieManager = CookieManager.getInstance()
            cookieManager.removeSessionCookies { aBoolean ->
            }
            cookieManager.removeAllCookies(ValueCallback<Boolean?> { value ->
            })
            cookieManager.flush()
        }

//        val address = intent.getStringExtra("wallet_address")
//        address?.let {
//            walletAddress = address
//            if (walletAddress.isNotBlank() && !token.isNullOrEmpty()) {
//                Log.d("wallet", "지갑주소 이미 있음 $walletAddress")
//                Toast.makeText(applicationContext, "wallet : $walletAddress", Toast.LENGTH_SHORT).show()
//                val intentW = Intent(applicationContext, MainActivity::class.java)
//                setResult(1, intentW)
//                finish()
//            }
//        }


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
            settings.domStorageEnabled =
                true // 로컬 스토리지 사용 여부를 설정하는 속성으로 팝업창등을 '하루동안 보지 않기' 기능 사용에 필요
            settings.allowContentAccess // 웹뷰 내에서 파일 액세스 활성화 여부
            settings.userAgentString = "app" // 웹에서 해당 속성을 통해 앱에서 띄운 웹뷰로 인지 할 수 있도록 합니다.
            settings.defaultTextEncodingName = "UTF-8" // 인코딩 설정
            settings.databaseEnabled = true //Database Storage API 사용 여부 설정
        }
        binding.oauthWebView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView,
                request: WebResourceRequest
            ): Boolean {
                if (request.url.toString().startsWith(appUrl)) {
                    val cookies = CookieManager.getInstance().getCookie(request.url.toString())
                    if (cookies != null) {
                        Log.d("cookie", "shouldoverrideurlloading : $cookies")
                    }
                    return false
                }
                Log.d("cookie", "url: ${request.url}")
                binding.oauthWebView.loadUrl(request.url.toString())
                return true
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                try{
                    if (!this@LoginActivity.isFinishing) {
                        val cookies = CookieManager.getInstance().getCookie(oauthUrl)
                        val cookie = CookieManager.getInstance()
                            .getCookie(appUrl)
                        Log.d("cookie", "$oauthUrl: $cookies  $appUrl: $cookie")
                        Log.d("cookie", "onPageFinished original url: $url")
                        Log.d("cookie", "onPageFinished original: $cookies")
                        if (cookies.contains("Access") && url!!.startsWith(appUrl)) {
                            val splits = cookies.split("; ")
                            val access = splits[1].split("=")[1]
                            val refresh = splits[2].split("=")[1]
                            Log.d("tokens", "access:$access, refresh:$refresh")
                            prefs.setJwtToken(access)
                            prefs.setRefreshToken(refresh)
                            if (walletAddress.isNotBlank()) {
                                val intentH = Intent(this@LoginActivity, MainActivity::class.java)
                                intentH.putExtra("access", access)
                                intentH.putExtra("refresh", refresh)
                                startActivity(intentH)
                            } else {
                                binding.loginGithub.visibility = View.GONE
                                binding.loginMain.visibility = View.VISIBLE
                                binding.githubAuth.isEnabled = false
                                checkState(prefs.getJwtToken(""), prefs.getRefreshToken(""))
                            }
                        }
                    }
                } catch (e: Exception) {
                    Log.d("webview error", "error ${e.message}")

                }
            }
        }
        binding.oauthWebView.webChromeClient = object : WebChromeClient() {
            override fun onCreateWindow(
                view: WebView?,
                isDialog: Boolean,
                isUserGesture: Boolean,
                resultMsg: Message?
            ): Boolean {
                val newWebView = WebView(view!!.context)
                val transport = resultMsg!!.obj as WebViewTransport
                transport.webView = newWebView
                resultMsg.sendToTarget()

                newWebView.webViewClient = object : WebViewClient() {
                    override fun shouldOverrideUrlLoading(
                        view: WebView,
                        request: WebResourceRequest
                    ): Boolean {
                        if (request.url.toString().startsWith(appUrl)) {
                            val cookies =
                                CookieManager.getInstance().getCookie(request.url.toString())
                            if (cookies != null) {
                                Log.d("cookie", "shouldoverrideurlloading : $cookies")
                            }
                            val intent = Intent(Intent.ACTION_VIEW, request.url)
                            startActivity(intent)
                            return false
                        }
                        binding.oauthWebView.loadUrl(request.url.toString())
                        return true
                    }

                    override fun onPageFinished(view: WebView?, url: String?) {
                        val cookies = CookieManager.getInstance().getCookie(url)
                        if (cookies != null) {
                            Log.d("cookie", "onPageFinished chrome: $cookies")
                        }
                    }

                }
                return true
            }
        }
        binding.githubAuth.setOnClickListener {
//            binding.loginMain.visibility = View.GONE
//            binding.loginGithub.visibility = View.VISIBLE
//            binding.oauthWebView.isEnabled = true
//            Log.d("이동", "웹뷰로 이동 ${BuildConfig.api}oauth2/authorize/github")
//            binding.oauthWebView.loadUrl("${BuildConfig.api}oauth2/authorize/github")

            if(::mClient.isInitialized) {
                val customTabsCallback = MyCustomTabsCallback()
                val sessions = mClient.newSession(customTabsCallback)
                val customTabsIntent = CustomTabsIntent.Builder()
                    .setSession(sessions!!)
                    .build()

                customTabsIntent.launchUrl(this@LoginActivity, Uri.parse(oauthUrl))
            }
//            val intentG = Intent(Intent.ACTION_VIEW, Uri.parse(oauthUrl))
//            startActivity(intentG)

        }
        binding.walletAuth.setOnClickListener {
            if (NetworkCheck.checkNetworkState(this)) {
                walletAuthRequest()
            }
        }

    }



    private val connection = object : CustomTabsServiceConnection() {
        override fun onCustomTabsServiceConnected(
            componentName: ComponentName,
            client: CustomTabsClient
        ) {
            CookieManager.getInstance().setAcceptCookie(true)
            Log.d("시작", "custom tab 시작")
            mClient = client
            mClient.warmup(0)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            // 서비스 연결 해제 시 필요한 작업을 수행합니다.
        }
    }

    private fun getDefaultBrowserPackageName(context: Context): String? {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("http://www.example.com"))
        val resolveInfoList = context.packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)

        for (resolveInfo in resolveInfoList) {
            // 첫 번째 매치를 사용하거나 원하는 로직에 따라 패키지를 선택할 수 있습니다.
            return resolveInfo.activityInfo.packageName
        }
        return null
    }

    inner class MyCustomTabsCallback : CustomTabsCallback() {
        override fun onNavigationEvent(navigationEvent: Int, extras: Bundle?) {
//            when (navigationEvent) {
//                CustomTabsCallback.NAVIGATION_STARTED -> {
//                    checkCookie(navigationEvent)
//                }
//                CustomTabsCallback.TAB_HIDDEN -> {
//                    checkCookie(navigationEvent)
//                }
//                CustomTabsCallback.NAVIGATION_FINISHED -> { checkCookie(navigationEvent)}
//                else -> {checkCookie(navigationEvent)}
//                // 기타 필요한 경우 처리
//            }
        }

    }

    private fun checkCookie(state: Int) {
        Log.d("state", "state: $state")
        val cookies = CookieManager.getInstance().getCookie(oauthUrl)
        val cookie = CookieManager.getInstance()
            .getCookie(appUrl)
        Log.d("start", "$oauthUrl: $cookies   $appUrl: $cookie")
        if (cookies != null && cookies.contains("Access")) {
            val splits = cookies.split("; ")
            val access = splits[1].split("=")[1]
            val refresh = splits[2].split("=")[1]
            Log.d("tokens", "access:$access, refresh:$refresh")
            prefs.setJwtToken(access)
            prefs.setRefreshToken(refresh)
            if (walletAddress.isNotBlank()) {
                val intentH = Intent(this@LoginActivity, MainActivity::class.java)
                intentH.putExtra("access", access)
                intentH.putExtra("refresh", refresh)
                startActivity(intentH)
            } else {
                Log.d("start", "access: $access, refresh: $refresh")
                binding.loginMain.visibility = View.VISIBLE
                checkState(prefs.getJwtToken(""), prefs.getRefreshToken(""))
            }
        }

        if (cookie != null && cookie.contains("Access") ) {
            val splits = cookie.split("; ")
            val access = splits[1].split("=")[1]
            val refresh = splits[2].split("=")[1]
            Log.d("tokens", "access:$access, refresh:$refresh")
            prefs.setJwtToken(access)
            prefs.setRefreshToken(refresh)
            if (walletAddress.isNotBlank()) {
                val intentH = Intent(this@LoginActivity, MainActivity::class.java)
                intentH.putExtra("access", access)
                intentH.putExtra("refresh", refresh)
                startActivity(intentH)
            } else {
                Log.d("start", "access: $access, refresh: $refresh")
                binding.loginMain.visibility = View.VISIBLE
                checkState(prefs.getJwtToken(""), prefs.getRefreshToken(""))
            }
        }
    }

    private fun checkState(token: String, refresh: String) {
        val coroutine = CoroutineScope(Dispatchers.Main)
        coroutine.launch {
            if (!this@LoginActivity.isFinishing) {
                val resultDeffered = coroutine.async(Dispatchers.IO) {
                    viewmodel.checkLoginState(token)
                }
                val result = resultDeffered.await()
                Log.d("info", "로그인 화면 token: $token")
                Log.d("info", "로그인 화면 refresh: $refresh")
                Log.d("info", "check key : $key")
                Log.d("로그인 상태", "로그인 상태 결과 : $result")
                when(result) {
                    true -> {
                        val intentF = Intent(applicationContext, MainActivity::class.java)
                        intentF.putExtra("access", token)
                        intentF.putExtra("refresh", refresh)
                        startActivity(intentF)
                        finish()
                    }
                    false -> {
                        if(key.isNotBlank()) {
                            val intentF = Intent(applicationContext, MainActivity::class.java)
                            Log.d("info", "key : $key")
                            intentF.putExtra("key", key)
                            intentF.putExtra("access", token)
                            intentF.putExtra("refresh", refresh)
                            startActivity(intentF)
                            finish()
                        } else {
                            binding.githubAuth.isEnabled = false
                            binding.walletAuth.isEnabled = true
//                        binding.walletFinish.isEnabled = true
                        }
                    }
                    null -> {
                        Log.d("로그인 상태 null", "null")
                        prefs.setJwtToken("")
                        prefs.setRefreshToken("")
                        binding.githubAuth.setTextColor(Color.WHITE)
                        binding.githubAuth.isEnabled = true

                        binding.walletAuth.isEnabled = false
                    }
                }
            }

        }
    }
    private fun walletAuthRequest() {
        val coroutine = CoroutineScope(Dispatchers.Main)
        coroutine.launch {
            if(!this@LoginActivity.isFinishing) {
                val authResponseDeffered = coroutine.async(Dispatchers.IO) {
                    viewmodel.postWalletAuth(body)
                }
                val authResponse = authResponseDeffered.await()
                if (authResponse.request_key.isNullOrEmpty() || authResponse.status != "prepared") {
                    val handler = Handler(Looper.getMainLooper())
                    handler.postDelayed({ walletAuthRequest() }, 1000)
                } else {
                    key = authResponse.request_key
                    prefs.setKey(authResponse.request_key)
                    Log.d("info", "key : ${authResponse.request_key}")
                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://klipwallet.com/?target=/a2a?request_key=${authResponse.request_key}")
                    )
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
//                    finishAffinity()
//                    moveTaskToBack(true); // 태스크를 백그라운드로 이동
//                    finishAndRemoveTask(); // 액티비티 종료 + 태스크 리스트에서 지우기
//                    Process.killProcess(Process.myPid())
                }
            }
        }
    }
    private val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (System.currentTimeMillis() > backPressed + 2500) {
                backPressed = System.currentTimeMillis()
                binding.loginGithub.visibility = View.GONE
                binding.loginMain.visibility = View.VISIBLE
                binding.oauthWebView.isEnabled = false
                Toast.makeText(applicationContext, "Back 버튼을 한번 더 누르면 종료합니다.", Toast.LENGTH_SHORT)
                    .show()
                return
            }

            if (System.currentTimeMillis() <= backPressed + 2500) {
                finishAffinity()
            }
        }
    }


    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val uri = intent?.data
        Log.d("uri new", "uri: $uri")
        if (uri != null && uri.toString().startsWith("$appUrl?")) {
            val urlSplit = uri.toString().split("?")
            val queries = urlSplit[1].split("&")
            val access = queries[0].split("=")[1]
            val refresh = queries[1].split("=")[1]
            prefs.setJwtToken(access)
            prefs.setRefreshToken(refresh)
            if (walletAddress.isNotBlank()) {
                val intentH = Intent(this@LoginActivity, MainActivity::class.java)
                intentH.putExtra("access", access)
                intentH.putExtra("refresh", refresh)
                startActivity(intentH)
            } else {
                Log.d("start", "access: $access, refresh: $refresh")
                binding.loginMain.visibility = View.VISIBLE
                checkState(prefs.getJwtToken(""), prefs.getRefreshToken(""))
            }
        } else {
            checkState(prefs.getJwtToken(""), prefs.getRefreshToken(""))
        }
    }

    override fun onRestart() {
        super.onRestart()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        this.unbindService(connection)
    }
}