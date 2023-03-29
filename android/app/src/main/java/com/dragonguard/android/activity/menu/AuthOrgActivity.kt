package com.dragonguard.android.activity.menu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import com.dragonguard.android.R
import com.dragonguard.android.activity.LoginActivity
import com.dragonguard.android.activity.MainActivity
import com.dragonguard.android.connect.NetworkCheck
import com.dragonguard.android.databinding.ActivityAuthOrgBinding
import com.dragonguard.android.viewmodel.Viewmodel

class AuthOrgActivity : AppCompatActivity() {
    private val activityResultLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == 0) {
                val orgIntent = it.data
                try {
                    val orgName = orgIntent!!.getStringExtra("orgName")
//            Toast.makeText(applicationContext, requestKey, Toast.LENGTH_SHORT).show()
                    if(orgName != null) {
                        binding.orgNameEdit.setText(orgName)
                        binding.orgNameEdit.isEnabled = false
                    }
                } catch (e: Exception) {
//                finishAffinity()
//                val intent = Intent(this, MainActivity::class.java)
//                startActivity(intent)
//                exitProcess(0)
                }

            }
        }
    private lateinit var binding: ActivityAuthOrgBinding
    private val viewmodel = Viewmodel()
    private var token = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_regist_org)
        binding.authOrgViewmodel = viewmodel

        setSupportActionBar(binding.toolbar) //커스텀한 toolbar를 액션바로 사용
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
        supportActionBar?.title = "   조직 등록"

        val arr1 : MutableList<String> = mutableListOf("선택하세요")
        arr1.apply {
            add("대학교")
            add("회사")
            add("etc")
        }
        val spinnerAdapter = ArrayAdapter<String>(applicationContext, R.layout.spinner_list, arr1)
        binding.orgTypeSpinner.adapter = spinnerAdapter
        binding.orgTypeSpinner.setSelection(0)

        token = intent.getStringExtra("token")!!

        binding.orgNameEdit.setOnClickListener {
            if(binding.orgTypeSpinner.selectedItem == "선택하세요") {
                Toast.makeText(applicationContext, "조직 타입을 먼저 정해주세요!!", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(applicationContext, SearchOrganizationActivity::class.java)
                intent.putExtra("token", token)
                intent.putExtra("type", binding.orgTypeSpinner.selectedItem.toString())
                activityResultLauncher.launch(intent)
            }
        }

        binding.authOrgBtn.setOnClickListener {
            if(binding.orgNameEdit.text.toString().isNotBlank() && binding.orgEmailEdit.text.toString().isNotBlank() && binding.orgTypeSpinner.selectedItem.toString().isNotBlank()) {
                var orgType = ""
                when (binding.orgTypeSpinner.selectedItem.toString()) {
                    "대학교" -> {
                        orgType = "UNIVERSITY"
                    }
                    "회사" -> {
                        orgType = "COMPANY"
                    }
                    "etc" -> {
                        orgType = "ETC"
                    }
                }
            } else {
                Toast.makeText(applicationContext, "miss!! name: ${binding.orgNameEdit.text}, email: ${binding.orgEmailEdit.text}, type: ${binding.orgTypeSpinner.selectedItem}", Toast.LENGTH_SHORT).show()
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