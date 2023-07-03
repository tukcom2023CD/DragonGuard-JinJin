package com.dragonguard.android.activity.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
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
}