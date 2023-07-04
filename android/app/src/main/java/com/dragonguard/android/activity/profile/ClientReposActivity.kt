package com.dragonguard.android.activity.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.dragonguard.android.R
import com.dragonguard.android.adapters.ClientGitOrgAdapter
import com.dragonguard.android.adapters.OthersReposAdapter
import com.dragonguard.android.databinding.ActivityClientReposBinding
import com.dragonguard.android.model.GithubOrgReposModel
import com.dragonguard.android.viewmodel.Viewmodel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ClientReposActivity : AppCompatActivity() {
    private lateinit var binding: ActivityClientReposBinding
    private var token = ""
    private var viewmodel = Viewmodel()
    private var orgName = ""
    private lateinit var reposAdapter: OthersReposAdapter
    private var img = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClientReposBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar) //커스텀한 toolbar를 액션바로 사용
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.back)

        intent.getStringExtra("token")?.let {
            token = it
        }
        intent.getStringExtra("orgName")?.let {
            orgName = it
        }
        intent.getStringExtra("img")?.let {
            img = it
        }
        if(token.isNotBlank() && orgName.isNotBlank() && img.isNotBlank()) {
            getClientRepos()
        }
    }

    private fun getClientRepos() {
        val coroutine = CoroutineScope(Dispatchers.Main)
        coroutine.launch {
            if (!this@ClientReposActivity.isFinishing) {
                val resultDeferred = coroutine.async(Dispatchers.IO) {
                    viewmodel.getOrgRepoList(orgName, token)
                }
                val result = resultDeferred.await()
                result?.let {
                    initRecycler(it)
                }

            }
        }
    }

    private fun initRecycler(result: GithubOrgReposModel) {
        reposAdapter = OthersReposAdapter(result.git_repos, this, token, img, orgName)
        binding.memberRepositoryList.adapter = reposAdapter
        binding.memberRepositoryList.layoutManager = LinearLayoutManager(this)
        reposAdapter.notifyDataSetChanged()
    }


    //    뒤로가기, 홈으로 화면전환 기능
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}