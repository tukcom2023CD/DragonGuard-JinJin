package com.dragonguard.android.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.dragonguard.android.R
import com.dragonguard.android.databinding.ActivityRepoContributorsBinding
import com.dragonguard.android.model.RepoContributorsItem
import com.dragonguard.android.viewmodel.RepoContributorsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class RepoContributorsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRepoContributorsBinding
    var viewmodel = RepoContributorsViewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_repo_contributors)
        binding.repoContributorsViewmodel = viewmodel

        setSupportActionBar(binding.toolbar) //커스텀한 toolbar를 액션바로 사용
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)

        val repoName = intent.getStringExtra("repoName")
        Toast.makeText(applicationContext,"reponame = $repoName",Toast.LENGTH_SHORT).show()
        repoContributors(repoName!!)
    }

    fun repoContributors(repoName: String) {
        val coroutine = CoroutineScope(Dispatchers.Main)
        coroutine.launch {
            var resultDeferred = coroutine.async(Dispatchers.IO) {
                viewmodel.getRepoContributors(repoName)
            }
            var result = resultDeferred.await()
            if(!checkContributors(result)) {
                resultDeferred = coroutine.async(Dispatchers.IO) {
                    viewmodel.getRepoContributors(repoName)
                }
                result = resultDeferred.await()
                checkContributors(result)
            }
        }
    }

    fun checkContributors(result: ArrayList<RepoContributorsItem>):Boolean {
        if(!result.isNullOrEmpty()) {
            Toast.makeText(applicationContext, "id = ${result[0].githubId} ",Toast.LENGTH_SHORT).show()
            return true
        }
        Log.d("결과", "비어있음")
        return false
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