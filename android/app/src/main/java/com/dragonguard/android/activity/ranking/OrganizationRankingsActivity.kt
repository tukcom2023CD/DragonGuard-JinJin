package com.dragonguard.android.activity.ranking

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import com.dragonguard.android.R
import com.dragonguard.android.activity.basic.MainActivity
import com.dragonguard.android.databinding.ActivityOrganizationRankingsBinding

class OrganizationRankingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOrganizationRankingsBinding
    private var token = ""
    private var orgName = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_organization_rankings)

        setSupportActionBar(binding.toolbar) //커스텀한 toolbar를 액션바로 사용
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)

        token = intent.getStringExtra("token")!!
        orgName = intent.getStringExtra("organization")!!

        binding.internalRanking.setOnClickListener {
            val intent = Intent(applicationContext, MyOrganizationInternalActivity::class.java)
            intent.putExtra("token", token)
            intent.putExtra("organization", orgName)
            startActivity(intent)
        }

        binding.totalOrgRanking.setOnClickListener {
            val intent = Intent(applicationContext, TotalOrgRankingActivity::class.java)
            intent.putExtra("token", token)
            startActivity(intent)
        }

        binding.universityRanking.setOnClickListener {
            val intent = Intent(applicationContext, TypeOrgRankingActivity::class.java)
            intent.putExtra("token", token)
            intent.putExtra("orgType", "UNIVERSITY")
            startActivity(intent)
        }

        binding.companyRanking.setOnClickListener {
            val intent = Intent(applicationContext, TypeOrgRankingActivity::class.java)
            intent.putExtra("token", token)
            intent.putExtra("orgType", "COMPANY")
            startActivity(intent)
        }


        binding.etcRanking.setOnClickListener {
            val intent = Intent(applicationContext, TypeOrgRankingActivity::class.java)
            intent.putExtra("token", token)
            intent.putExtra("orgType", "ETC")
            startActivity(intent)
        }
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