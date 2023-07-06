package com.dragonguard.android.activity.menu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import com.dragonguard.android.R
import com.dragonguard.android.activity.basic.MainActivity
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
        supportActionBar?.setHomeAsUpIndicator(R.drawable.back)
        supportActionBar?.title = "조직 등록 요청 승인"

        approveFragment = ApproveOrgFragment(token)
        approvedFragment = ApprovedOrgFragment(token)

        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.approve_org_list, ApproveOrgFragment(token))
            .commit()

        binding.bottomNavigation.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.approve_request_btn -> {
                    supportActionBar?.title = "승인 대기중인 조직 목록"
                    val transactionN = supportFragmentManager.beginTransaction()
                    transactionN.replace(R.id.approve_org_list, ApproveOrgFragment(token))
                        .commit()
                }
                R.id.approve_finished_btn -> {
                    supportActionBar?.title = "승인된 조직 목록"
                    val transactionN = supportFragmentManager.beginTransaction()
                    transactionN.replace(R.id.approve_org_list, ApprovedOrgFragment(token))
                        .commit()
                }
            }
            true
        }
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