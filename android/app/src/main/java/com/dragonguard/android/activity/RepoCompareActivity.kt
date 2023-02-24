package com.dragonguard.android.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.dragonguard.android.R
import com.dragonguard.android.viewmodel.Viewmodel
import com.dragonguard.android.databinding.ActivityRepoCompareBinding
import com.dragonguard.android.fragment.CompareRepoFragment
import com.dragonguard.android.fragment.CompareUserFragment
import com.dragonguard.android.model.RepoContributorsItem
import com.dragonguard.android.recycleradapter.ContributorsAdapter
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class RepoCompareActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRepoCompareBinding
    var viewmodel = Viewmodel()
    private var repo1 = ""
    private var repo2 = ""
    private lateinit var compareUserFragment: CompareUserFragment
    private lateinit var compareRepoFragment: CompareRepoFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_repo_compare)
        binding.repoCompareViewModel = viewmodel

        val intent = getIntent()
        repo1 = intent.getStringExtra("repo1")!!
        repo2 = intent.getStringExtra("repo2")!!
//        Toast.makeText(applicationContext, "repo1 : $repo1 repo2 : $repo2", Toast.LENGTH_SHORT).show()

//        val myFragment = supportFragmentManager.findFragmentById(R.id.compare_frame) as CompareUserFragment
        compareRepoFragment = CompareRepoFragment(repo1, repo2)
        compareUserFragment = CompareUserFragment(repo1, repo2)
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.compare_frame, compareRepoFragment)
            .add(R.id.compare_frame, compareUserFragment)
            .replace(R.id.compare_frame, compareRepoFragment)
            .commit()

        binding.bottomNavigation.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.compare_repo -> {
                    val transactionN = supportFragmentManager.beginTransaction()
                    transactionN.replace(binding.compareFrame.id, compareRepoFragment)
                        .commit()
                }
                R.id.compare_user -> {
                    val transactionN = supportFragmentManager.beginTransaction()
                    transactionN.replace(binding.compareFrame.id, compareUserFragment)
                        .commit()
                }
            }
            true
        }

        setSupportActionBar(binding.toolbar) //커스텀한 toolbar를 액션바로 사용
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)

    }


    //    뒤로가기 누르면 화면 전환하게 함
    override fun onBackPressed() {
        val intent = Intent(applicationContext, RepoChooseActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        startActivity(intent)
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