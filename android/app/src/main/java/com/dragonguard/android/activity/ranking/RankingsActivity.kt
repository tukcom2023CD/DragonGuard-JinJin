package com.dragonguard.android.activity.ranking

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import com.dragonguard.android.R
import com.dragonguard.android.activity.basic.MainActivity
import com.dragonguard.android.databinding.ActivityRankingsBinding

/*
 보고싶은 랭킹을 보러 가기 위한 activity
 */
class RankingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRankingsBinding
    private var token = ""
    private var orgName = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_rankings)

        token = intent.getStringExtra("token")!!
        orgName = intent.getStringExtra("organization")!!

        setSupportActionBar(binding.toolbar) //커스텀한 toolbar를 액션바로 사용
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)

//        각각 버튼을 누르면 원하는 랭킹을 보러 가게 화면 전환
        binding.myRepoRanking.setOnClickListener {
            val intent = Intent(applicationContext, MyRepoRankingActivity::class.java)
            intent.putExtra("token", token)
            startActivity(intent)
        }

        binding.myOrganizationList.setOnClickListener {
            val intent = Intent(applicationContext, MyOrganizationListActivity::class.java)
            intent.putExtra("token", token)
            startActivity(intent)
        }

        binding.orgRelatedRankings.setOnClickListener {
            val intent = Intent(applicationContext, OrganizationRankingsActivity::class.java)
            intent.putExtra("organization", orgName)
            intent.putExtra("token", token)
            startActivity(intent)
        }

        binding.userRanking.setOnClickListener {
            val intent = Intent(applicationContext, TotalUsersRankingActivity::class.java)
            intent.putExtra("token", token)
            startActivity(intent)
        }

        binding.totalRepoRanking.setOnClickListener {
            val intent = Intent(applicationContext, TotalRepoRankingActivity::class.java)
            intent.putExtra("token", token)
            startActivity(intent)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home, binding.toolbar.menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home->{
                finish()
            }
            R.id.home_menu->{
                val intent = Intent(applicationContext, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}