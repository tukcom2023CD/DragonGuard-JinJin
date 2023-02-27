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
import com.dragonguard.android.model.CompareRepoMembersResponseModel
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
    private var count = 0
    private lateinit var compareUserFragment: CompareUserFragment
    private lateinit var compareRepoFragment: CompareRepoFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_repo_compare)
        binding.repoCompareViewModel = viewmodel

        val intent = getIntent()
        repo1 = intent.getStringExtra("repo1")!!
        repo2 = intent.getStringExtra("repo2")!!
        repoContributors()

//        Toast.makeText(applicationContext, "repo1 : $repo1 repo2 : $repo2", Toast.LENGTH_SHORT).show()

//        val myFragment = supportFragmentManager.findFragmentById(R.id.compare_frame) as CompareUserFragment


        setSupportActionBar(binding.toolbar) //커스텀한 toolbar를 액션바로 사용
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)

    }

    fun repoContributors() {
        val coroutine = CoroutineScope(Dispatchers.Main)
        coroutine.launch {
            val resultDeferred = coroutine.async(Dispatchers.IO) {
                viewmodel.postCompareRepoMembersRequest(repo1, repo2)
            }
            val result = resultDeferred.await()
//            Toast.makeText(applicationContext, "result = ${result.size}",Toast.LENGTH_SHORT).show()
            checkContributors(result)
        }
    }

    fun checkContributors(result: CompareRepoMembersResponseModel) {
        if ((result.firstResult != null) && (result.secondResult != null)) {
            if (result.firstResult.isEmpty()) {
                count++
                val handler = Handler(Looper.getMainLooper())
                handler.postDelayed({repoContributors()}, 2000)
            } else {
                startFragment()
            }
        } else {
            if(count<10) {
                count++
                val handler = Handler(Looper.getMainLooper())
                handler.postDelayed({repoContributors()}, 2000)
            }
        }
    }

    private fun startFragment() {
        compareRepoFragment = CompareRepoFragment(repo1, repo2)
        compareUserFragment = CompareUserFragment(repo1, repo2)
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.compare_frame, compareRepoFragment)
            .add(R.id.compare_frame, compareUserFragment)
            .hide(compareUserFragment)
            .commit()

        binding.bottomNavigation.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.compare_repo -> {
                    binding.compareTitle.text = "Repository 비교"
                    val transactionN = supportFragmentManager.beginTransaction()
                    transactionN.hide(compareUserFragment)
                        .show(compareRepoFragment)
                        .commit()
                }
                R.id.compare_user -> {
                    binding.compareTitle.text = "Repository 구성원 비교"
                    val transactionN = supportFragmentManager.beginTransaction()
                    transactionN.show(compareUserFragment)
                        .hide(compareRepoFragment)
                        .commit()
                }
            }
            true
        }
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