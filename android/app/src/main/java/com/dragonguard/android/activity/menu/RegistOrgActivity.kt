package com.dragonguard.android.activity.menu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.dragonguard.android.R
import com.dragonguard.android.activity.MainActivity
import com.dragonguard.android.databinding.ActivityRegistOrgBinding
import com.dragonguard.android.viewmodel.Viewmodel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class RegistOrgActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegistOrgBinding
    private var token = ""
    private var viewmodel = Viewmodel()
    private var registLimit = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_regist_org)
        binding.registOrgViewmodel = viewmodel

        token = intent.getStringExtra("token")!!

        setSupportActionBar(binding.toolbar) //커스텀한 toolbar를 액션바로 사용
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
        supportActionBar?.title = "   조직 등록"

        binding.regitstOrgBtn.setOnClickListener {
            if(binding.orgNameEdit.text.toString().isNotBlank() && binding.orgEmailEdit.text.toString().isNotBlank() && binding.orgTypeSpinner.selectedItem.toString().isBlank()) {
                registOrg(binding.orgNameEdit.text.toString(), binding.orgTypeSpinner.selectedItem.toString(), binding.orgEmailEdit.text.toString())
            } else {
                Toast.makeText(applicationContext, "등록할 조직의 정보를 다 채워주세요!!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun registOrg(name: String, orgType: String, orgEmail: String) {
        val coroutine = CoroutineScope(Dispatchers.Main)
        coroutine.launch {
            val resultDeferred = coroutine.async(Dispatchers.IO) {
                viewmodel.registerOrg(name,orgType,orgEmail, token)
            }
            val result = resultDeferred.await()
            if(result.id == 0 && registLimit<3) {
                registLimit++
                registOrg(name, orgType, orgEmail)
            } else {
                Toast.makeText(applicationContext, "$name 등록 완료!", Toast.LENGTH_SHORT).show()
                val intent = Intent(applicationContext, MenuActivity::class.java)
                intent.putExtra("token", token)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                startActivity(intent)
            }
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        closeKeyboard()
        return super.dispatchTouchEvent(ev)
    }

    //    edittext의 키보드 제거
    fun closeKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.orgEmailEdit.windowToken, 0)
        imm.hideSoftInputFromWindow(binding.orgNameEdit.windowToken, 0)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home, binding.toolbar.menu)
        return true
    }

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