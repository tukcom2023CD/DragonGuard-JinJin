package com.dragonguard.android.activity.menu

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.dragonguard.android.R
import com.dragonguard.android.activity.basic.MainActivity
import com.dragonguard.android.databinding.ActivityMenuBinding
import com.dragonguard.android.viewmodel.Viewmodel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

/*
 메인화면에서 프로필 사진이나 id를 눌렀을 때 메뉴를 보여주는 activity
 */
class MenuActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMenuBinding
    private var viewmodel = Viewmodel()
    private lateinit var versionDialog : Dialog
    private var token = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_menu)
        binding.menuViewmodel = viewmodel

        checkAdmin()
        versionDialog = Dialog(this)
        versionDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        versionDialog.setContentView(R.layout.version_dialog)
        val version = versionDialog.findViewById<TextView>(R.id.gitrank_version)
        version.append("v1.1.3")

        setSupportActionBar(binding.toolbar) //커스텀한 toolbar를 액션바로 사용
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.back)

        token = intent.getStringExtra("token")!!

//        로그아웃버튼 누르면 로그아웃 기능
        binding.logout.setOnClickListener {
            val intent = Intent(applicationContext, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            intent.putExtra("logout", true)
            startActivity(intent)
        }

//        faq버튼 누르면 faq 화면으로 전환
        binding.faq.setOnClickListener {
            val intent = Intent(applicationContext, FaqActivity::class.java)
            startActivity(intent)
        }

//        토큰부여기준버튼 누르면 화면 전환
        binding.tokenCriterion.setOnClickListener {
            val intent = Intent(applicationContext, CriterionActivity::class.java)
            startActivity(intent)
        }

//        버전버튼 누르면 dialog 띄움
        binding.version.setOnClickListener {
            showDialog()
        }

        binding.organizationAuth.setOnClickListener {
            val intent = Intent(applicationContext, AuthOrgActivity::class.java)
            intent.putExtra("token", token)
            startActivity(intent)
        }
        binding.organizationApprove.setOnClickListener {
            val intent = Intent(applicationContext, ApprovalOrgActivity::class.java)
            intent.putExtra("token", token)
            startActivity(intent)
        }
        binding.withdrawBtn.setOnClickListener {
            withDraw()
        }
    }

    private fun checkAdmin() {
        val coroutine = CoroutineScope(Dispatchers.Main)
        coroutine.launch {
            if(!this@MenuActivity.isFinishing) {
                val resultDeferred = coroutine.async(Dispatchers.IO){
                    viewmodel.checkAdmin(token)
                }
                val result = resultDeferred.await()
                Log.d("admin", "admin: $result")
                if(result) {
                    binding.adminFun.visibility = View.VISIBLE
                    binding.withdrawFrame.visibility = View.GONE
                } else {
                    binding.adminFun.visibility = View.GONE
                    binding.withdrawFrame.visibility = View.VISIBLE
                }
            }
        }
    }

    //    버전 정보 보여주는 dialog 띄우기
    private fun showDialog() {
        versionDialog.show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home->{
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun withDraw() {
        val coroutine = CoroutineScope(Dispatchers.Main)
        coroutine.launch {
            if(!this@MenuActivity.isFinishing) {
                val resultDeferred = coroutine.async(Dispatchers.IO){
                    viewmodel.withDrawAccount(token)
                }
                val result = resultDeferred.await()
                Log.d("withdraw", "withdraw: $result")
                if(result) {
                    Handler(Looper.getMainLooper()).postDelayed({val intent = Intent(applicationContext, MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                        intent.putExtra("logout", true)
                        startActivity(intent)}, 1000)
                }
            }
        }
    }
}