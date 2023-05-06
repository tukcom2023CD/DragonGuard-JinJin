package com.dragonguard.android.activity.menu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import com.dragonguard.android.R
import com.dragonguard.android.activity.MainActivity
import com.dragonguard.android.databinding.ActivityApprovalOrgBinding
import com.dragonguard.android.fragment.ApproveOrgFragment
import com.dragonguard.android.fragment.ApprovedOrgFragment
import com.dragonguard.android.viewmodel.Viewmodel

class ApprovalOrgActivity : AppCompatActivity() {
    private lateinit var binding: ActivityApprovalOrgBinding
    private var viewmodel = Viewmodel()
    private lateinit var approveFragment: ApproveOrgFragment
    private lateinit var approvedFragment: ApprovedOrgFragment
    private var token = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding  = DataBindingUtil.setContentView(this, R.layout.activity_approval_org)
        binding.approvalOrgViewmodel = viewmodel

        token = intent.getStringExtra("token")!!

        setSupportActionBar(binding.toolbar) //커스텀한 toolbar를 액션바로 사용
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
        supportActionBar?.title = "조직 등록 요청 승인"

        approveFragment = ApproveOrgFragment(token)
        approvedFragment = ApprovedOrgFragment(token)

        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.approve_org_list, approveFragment)
            .add(R.id.approve_org_list, approvedFragment)
            .hide(approvedFragment)
            .commit()

        binding.bottomNavigation.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.approve_request_btn -> {
                    supportActionBar?.title = "승인 대기중인 조직 목록"
                    val transactionN = supportFragmentManager.beginTransaction()
                    transactionN.hide(approvedFragment)
                        .show(approveFragment)
                        .commit()
                }
                R.id.approve_finished_btn -> {
                    supportActionBar?.title = "승인된 조직 목록"
                    val transactionN = supportFragmentManager.beginTransaction()
                    transactionN.show(approvedFragment)
                        .hide(approveFragment)
                        .commit()
                }
            }
            true
        }
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