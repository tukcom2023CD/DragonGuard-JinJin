package com.dragonguard.android.activity.menu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.dragonguard.android.R
import com.dragonguard.android.activity.basic.MainActivity
import com.dragonguard.android.databinding.ActivityAuthEmailBinding
import com.dragonguard.android.viewmodel.Viewmodel
import com.dragonguard.android.viewmodel.Viewmodel.Companion.MIllIS_IN_FUTURE
import com.dragonguard.android.viewmodel.Viewmodel.Companion.TICK_INTERVAL
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class AuthEmailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthEmailBinding
    private val viewmodel = Viewmodel()
    private var token = ""
    private var orgName = ""
    private var email = ""
    private var emailAuthId: Long = 0
    private var orgId: Long = 0
    private lateinit var timer: CountDownTimer
    private var reset = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_auth_email)
        binding.authEamilViewmodel = viewmodel

        orgId = intent.getLongExtra("orgId", -1)
        email = intent.getStringExtra("email")!!
        token = intent.getStringExtra("token")!!
        orgName = intent.getStringExtra("orgName")!!


        setSupportActionBar(binding.toolbar) //커스텀한 toolbar를 액션바로 사용
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.back)
        supportActionBar?.title = "   이메일 인증"

        val coroutine = CoroutineScope(Dispatchers.Main)
        coroutine.launch {
            if(!this@AuthEmailActivity.isFinishing) {
                val resultDeferred = coroutine.async(Dispatchers.IO) {
                    viewmodel.addOrgMember(orgId, email, token)
                }
                val result = resultDeferred.await()
//                Toast.makeText(applicationContext, result.toString(), Toast.LENGTH_SHORT).show()
                if(result != -1L) {
                    emailAuthId = result
                    setUpCountDownTimer()
                    timer.start()
                }
            }
        }


        binding.resendCode.setOnClickListener {
            reset = false
            timer.cancel()
            setUpCountDownTimer()
            if(reset) {
                sendEmail()
            } else {
                deleteEmail()
                sendEmail()
            }
        }
        viewmodel.onAuthEmailListener.observe(this, Observer {
            if(viewmodel.onAuthEmailListener.value.toString().length ==5) {
                Log.d("request", "code: ${binding.emailCode.text}, token: $token, orgId: $emailAuthId")
                authEmail()
            }
        })
    }

    private fun authEmail() {
        val coroutine = CoroutineScope(Dispatchers.Main)
        coroutine.launch {
            val resultDeferred = coroutine.async(Dispatchers.IO) {
                viewmodel.emailAuthResult(emailAuthId, binding.emailCode.text.toString(), orgId ,token)
            }
            val result = resultDeferred.await()
            if(result) {
                Toast.makeText(applicationContext, "$orgName 인증되었습니다!!", Toast.LENGTH_SHORT).show()
                val intent = Intent(applicationContext, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                startActivity(intent)
            } else {
                binding.authStatus.text = "다시 입력해주세요!!"
                binding.emailCode.setText("")
            }
        }
    }

    private fun deleteEmail() {
        val coroutine = CoroutineScope(Dispatchers.Main)
        coroutine.launch {
            if(!this@AuthEmailActivity.isFinishing) {
                val resultDeferred = coroutine.async(Dispatchers.IO) {
                    viewmodel.deleteLateEmailCode(emailAuthId, token)
                }
                val result = resultDeferred.await()
                reset = result
            }
        }
    }

    private fun sendEmail() {
        val coroutine = CoroutineScope(Dispatchers.Main)
        coroutine.launch {
            if(!this@AuthEmailActivity.isFinishing) {
                val resultDeferred = coroutine.async(Dispatchers.IO) {
                    viewmodel.sendEmailAuth(token)
                }
                val result = resultDeferred.await()
                if(result != -1L) {
                    emailAuthId = result
                    timer.start()
                }
            }
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        closeKeyboard()
        return super.dispatchTouchEvent(ev)
    }

    private fun setUpCountDownTimer() {
        timer = object : CountDownTimer(MIllIS_IN_FUTURE, TICK_INTERVAL) {
            override fun onTick(millisUntilFinished: Long) {
                val minute = millisUntilFinished/60000L
                val second = millisUntilFinished%60000L/1000L
                binding.remainTime.text = "${minute}:${second}"
                viewmodel.timerJob.start()
            }

            override fun onFinish() {
                binding.authStatus.text = "시간초과!! 재전송을 눌러주세요!!"
                binding.emailCode.isEnabled = false
            }
        }
    }

    //    edittext의 키보드 제거
    fun closeKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.emailCode.windowToken, 0)
    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.home, binding.toolbar.menu)
//        return true
//    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }

        }
        return super.onOptionsItemSelected(item)
    }
}