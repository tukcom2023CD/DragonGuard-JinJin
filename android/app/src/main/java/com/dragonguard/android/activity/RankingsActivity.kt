package com.dragonguard.android.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import com.dragonguard.android.R
import com.dragonguard.android.databinding.ActivityRankingsBinding

class RankingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRankingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_rankings)


        setSupportActionBar(binding.toolbar) //커스텀한 toolbar를 액션바로 사용
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)

        binding.myRepoRanking.setOnClickListener {
            val intent = Intent(applicationContext, MyRepoRankingActivity::class.java)
            startActivity(intent)
        }

        binding.myOrganizationList.setOnClickListener {
            val intent = Intent(applicationContext, MyOrganizationListActivity::class.java)
            startActivity(intent)
        }

        binding.myUniversityInternalRanking.setOnClickListener {
            val intent = Intent(applicationContext, MyUniversityInternalActivity::class.java)
            startActivity(intent)
        }
        binding.totalUniversitiesRanking.setOnClickListener {
            val intent = Intent(applicationContext, TotalUniversitiesRankingActivity::class.java)
            startActivity(intent)
        }

        binding.userRanking.setOnClickListener {
            val intent = Intent(applicationContext, UserRankingActivity::class.java)
            startActivity(intent)
        }

        binding.totalRepoRanking.setOnClickListener {
            val intent = Intent(applicationContext, TotalRepoRankingActivity::class.java)
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