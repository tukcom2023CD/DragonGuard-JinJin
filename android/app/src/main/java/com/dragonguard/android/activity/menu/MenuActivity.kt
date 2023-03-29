package com.dragonguard.android.activity.menu

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.Window
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.dragonguard.android.R
import com.dragonguard.android.activity.MainActivity
import com.dragonguard.android.activity.compare.RepoChooseActivity
import com.dragonguard.android.databinding.ActivityMenuBinding

/*
 메인화면에서 프로필 사진이나 id를 눌렀을 때 메뉴를 보여주는 activity
 */
class MenuActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMenuBinding
    private lateinit var versionDialog : Dialog
    private var token = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_menu)

        versionDialog = Dialog(this)
        versionDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        versionDialog.setContentView(R.layout.version_dialog)
        val version = versionDialog.findViewById<TextView>(R.id.gitrank_version)
        version.append("v1.0")

        setSupportActionBar(binding.toolbar) //커스텀한 toolbar를 액션바로 사용
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)

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

    }

//    버전 정보 보여주는 dialog 띄우기
    private fun showDialog() {
        versionDialog.show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home->{
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}