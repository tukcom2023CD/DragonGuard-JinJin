package com.dragonguard.android.activity.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.dragonguard.android.R
import com.dragonguard.android.adapters.OthersReposAdapter
import com.dragonguard.android.databinding.ActivityUserProfileBinding
import com.dragonguard.android.model.detail.UserProfileModel
import com.dragonguard.android.viewmodel.Viewmodel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class UserProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserProfileBinding
    private var name = ""
    private var token = ""
    private lateinit var othersReposAdapter: OthersReposAdapter
    private var viewmodel = Viewmodel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent.getStringExtra("userName")?.let {
            name = it
        }
        intent.getStringExtra("token")?.let {
            token = it
        }

        setSupportActionBar(binding.toolbar) //커스텀한 toolbar를 액션바로 사용
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.back)
        supportActionBar?.setTitle(name)
        binding.profileImg.clipToOutline = true
        othersProfile()

    }

    private fun othersProfile() {
        Log.d("id", "id = $name")
        Log.d("token", "token = $token")
        val coroutine = CoroutineScope(Dispatchers.Main)
        coroutine.launch {
            if(!this@UserProfileActivity.isFinishing) {
                val resultDeferred = coroutine.async(Dispatchers.IO) {
                    viewmodel.othersProfile(name, token)
                }
                val result = resultDeferred.await()
                Log.d("result", "result = $result")
                result?.let {
                    Glide.with(binding.profileImg).load(it.profile_image)
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(binding.profileImg)
                    binding.userRank.text = it.rank.toString()
                    binding.userCommit.text = it.commits.toString()
                    binding.userIssue.text = it.issues.toString()
                    it.organization?.let {org->
                        binding.userOgr.text = org
                        binding.userOgr.visibility = View.VISIBLE
                    }
                    initRecycler(it)
                }
            }
        }
    }

    private fun initRecycler(result: UserProfileModel) {
        othersReposAdapter = OthersReposAdapter(result.git_repos, this, token, result.profile_image, name)
        binding.userRepoList.adapter = othersReposAdapter
        binding.userRepoList.layoutManager = LinearLayoutManager(this)
        binding.userRepoList.visibility = View.VISIBLE
        othersReposAdapter.notifyDataSetChanged()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}