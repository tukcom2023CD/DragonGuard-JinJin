package com.dragonguard.android.activity.compare

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.databinding.DataBindingUtil
import com.dragonguard.android.R
import com.dragonguard.android.activity.basic.MainActivity
import com.dragonguard.android.viewmodel.Viewmodel
import com.dragonguard.android.databinding.ActivityRepoCompareBinding
import com.dragonguard.android.fragment.CompareRepoFragment
import com.dragonguard.android.fragment.CompareUserFragment
import com.dragonguard.android.model.compare.CompareRepoMembersResponseModel
import com.dragonguard.android.adapters.CompareAdapter
import com.dragonguard.android.model.compare.CompareRepoResponseModel
import com.dragonguard.android.model.compare.RepoMembersResult
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class RepoCompareActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRepoCompareBinding
    private lateinit var adapter: CompareAdapter
    var viewmodel = Viewmodel()
    private var repo1 = ""
    private var repo2 = ""
    private var count = 0
    private lateinit var compareUserFragment: CompareUserFragment
    private lateinit var compareRepoFragment: CompareRepoFragment
    private var token = ""
    private var refresh = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_repo_compare)
        binding.repoCompareViewModel = viewmodel

        repo1 = intent.getStringExtra("repo1")!!
        repo2 = intent.getStringExtra("repo2")!!
        token = intent.getStringExtra("token")!!
        //Toast.makeText(applicationContext, "token : $token", Toast.LENGTH_SHORT).show()
        repoContributors()

//        Toast.makeText(applicationContext, "repo1 : $repo1 repo2 : $repo2", Toast.LENGTH_SHORT).show()

//        val myFragment = supportFragmentManager.findFragmentById(R.id.compare_frame) as CompareUserFragment
        setSupportActionBar(binding.toolbar) //커스텀한 toolbar를 액션바로 사용
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.back)
        supportActionBar?.title = "Repository 비교"

    }

    fun repoContributors() {
        val coroutine = CoroutineScope(Dispatchers.Main)
        coroutine.launch {
            if(!this@RepoCompareActivity.isFinishing) {
                val resultDeferred = coroutine.async(Dispatchers.IO) {
                    viewmodel.postCompareRepoMembersRequest(repo1, repo2, token)
                }
                val result = resultDeferred.await()
//            Toast.makeText(applicationContext, "result = ${result.size}",Toast.LENGTH_SHORT).show()
                checkContributors(result)
            }
        }
    }

    fun checkContributors(result: CompareRepoMembersResponseModel) {
        if ((result.first_result != null) && (result.second_result != null)) {
            if (result.first_result.isEmpty() || result.second_result.isEmpty()) {
                count++
                val handler = Handler(Looper.getMainLooper())
                handler.postDelayed({repoContributors()}, 2000)
            } else {
                startFragment(result.first_result, result.second_result)
            }
        } else {
            if(count<10) {
                count++
                val handler = Handler(Looper.getMainLooper())
                handler.postDelayed({repoContributors()}, 2000)
            }
        }
    }

    private fun startFragment(resultFirst: List<RepoMembersResult>, resultSecond: List<RepoMembersResult>) {
        binding.rankingLottie.pauseAnimation()
        binding.rankingLottie.visibility = View.GONE
        binding.compareFrame.visibility = View.VISIBLE
        compareRepoFragment = CompareRepoFragment(repo1, repo2, token, resultFirst, resultSecond)
        compareUserFragment = CompareUserFragment(repo1, repo2, token)
        adapter = CompareAdapter(this, token)

        adapter.addFragment(compareRepoFragment)
        adapter.addFragment(compareUserFragment)

        binding.fragmentContent.adapter = adapter
        val tabs = arrayOf("Repository", "User")
        TabLayoutMediator(binding.compareTab, binding.fragmentContent) { tab, position ->
            tab.text = tabs[position]
        }.attach()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.refresh, binding.toolbar.menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
            }
            R.id.refresh_button -> {
                if(refresh) {
                    refresh = false
                    val coroutine = CoroutineScope(Dispatchers.Main)
                    coroutine.launch {
                        binding.rankingLottie.resumeAnimation()
                        binding.rankingLottie.visibility = View.VISIBLE
                        binding.compareFrame.visibility = View.GONE
                        if(!this@RepoCompareActivity.isFinishing) {
                            val resultRepoDeferred = coroutine.async(Dispatchers.IO) {
                                viewmodel.updateCompareRepo(repo1, repo2, token)
                            }
                            val resultRepo = resultRepoDeferred.await()

                            val resultMemberDeferred = coroutine.async(Dispatchers.IO) {
                                viewmodel.updateCompareMembers(repo1, repo2, token)
                            }
                            val resultMember = resultMemberDeferred.await()
                            checkContributors(resultMember)
                        }
                    }
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

}