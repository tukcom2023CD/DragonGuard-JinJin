package com.dragonguard.android.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.dragonguard.android.R
import com.dragonguard.android.viewmodel.Viewmodel
import com.dragonguard.android.databinding.ActivityUserDetailBinding
import com.dragonguard.android.recycleradapter.UserRepoAdapter
import com.google.android.material.chip.Chip
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class UserDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserDetailBinding
    private var viewmodel = Viewmodel()
    private var token = ""
    private var githubId = ""
    private var userName: String? = ""
    private var count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_detail)

        try {
            token = intent.getStringExtra("token")!!
            githubId = intent.getStringExtra("githubId")!!
            userName = intent.getStringExtra("userName")
            userDetailInfo()
        } catch (e: Exception) {
            Toast.makeText(applicationContext, "토큰 혹은 userId 오류!!", Toast.LENGTH_SHORT).show()
            finish()
        }


        setSupportActionBar(binding.toolbar) //커스텀한 toolbar를 액션바로 사용
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
        supportActionBar?.title = "    사용자 상세조회"
    }

    private fun userDetailInfo() {
        val coroutine = CoroutineScope(Dispatchers.Main)
        coroutine.launch {
            //본인 프로필 확인
            if(userName != null) {
                val resultDeferred = coroutine.async(Dispatchers.IO) {
                    viewmodel.userDetails(githubId, token)
                }
                val userDetails = resultDeferred.await()

                // 실제 데이터 왔는지 확인    왔으면 깃허브 프로필 이미지, githubId, 사용자의 깃허브 랭킹 commit, issue pr, comment등을 보여줌
                if (userDetails.githubId != null && userDetails.commits != null && userDetails.issues != null && userDetails.profileImage != null &&
                    userDetails.pullRequests != null && userDetails.comments != null
                ) {
                    if (!this@UserDetailActivity.isFinishing) {
                        Glide.with(binding.githubProfile).load(userDetails.profileImage)
                            .into(binding.githubProfile)
                    }

                    binding.userId.text = userDetails.githubId
                    val chipCommit = Chip(applicationContext)
                    chipCommit.text = "Commit ${userDetails.commits}"

                    val chipIssues = Chip(applicationContext)
                    chipIssues.text = "Issues ${userDetails.issues}"

                    val chipPR = Chip(applicationContext)
                    chipPR.text = "PR ${userDetails.pullRequests}"

                    val chipComment = Chip(applicationContext)
                    chipComment.text = "Comments ${userDetails.comments}"

                    binding.userContribute.apply {
                        addView(chipCommit)
                        addView(chipIssues)
                        addView(chipPR)
                        addView(chipComment)
                    }
                    binding.userTier.text = userDetails.tier

                    if (userDetails.tokenAmount != null) {
                        when (userDetails.tokenAmount) {
                            0 -> {
                                binding.userArc.visibility = View.INVISIBLE
                            }
                            in 1..49 -> {
                                binding.progressStart.text = "1"
                                binding.progressEnd.text = "49"
                                binding.userArc.max = 49
                                binding.userArc.progress = userDetails.tokenAmount
                            }
                            in 50..199 -> {
                                binding.progressStart.text = "50"
                                binding.progressEnd.text = "199"
                                binding.userArc.max = 150
                                binding.userArc.progress = userDetails.tokenAmount - 50
                            }
                            in 200..499 -> {
                                binding.progressStart.text = "200"
                                binding.progressEnd.text = "499"
                                binding.userArc.max = 300
                                binding.userArc.progress = userDetails.tokenAmount - 200
                            }
                            in 500..1000 -> {
                                binding.progressStart.text = "500"
                                binding.progressEnd.text = "1000"
                                binding.userArc.max = 501
                                binding.userArc.progress = userDetails.tokenAmount - 500
                            }
                        }
                        binding.userArc.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                            override fun onProgressChanged(seekBar: SeekBar?, p1: Int, p2: Boolean) {
                                val value = seekBar?.progress
                                binding.currentProgress.text = userDetails.tokenAmount.toString()
                                if (value != 0) {
                                    val cur = seekBar!!.width / seekBar.max
                                    binding.currentProgress.x = (cur * value!!).toFloat() + 10
                                }
                                binding.currentProgress.y = seekBar?.pivotY!! + 10
                                Log.d(
                                    "Pos",
                                    binding.currentProgress.x.toString() + ": " + seekBar!!.width + ":" + seekBar.x
                                )
                            }
                            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
                        })
                    }
                    binding.userArc.isEnabled = false

                    // 조직 인증 완료시 속한 조직 이름과 전체 조직에서의 속한 조직의 랭킹 출력
                    if (userDetails.organization != null) {
                        binding.orgName.text = userDetails.organization
                        binding.orgRanking.text = userDetails.organizationRank.toString()
                    }

                    if (!userDetails.gitRepos.isNullOrEmpty()) {
                        binding.userRepositoryList.adapter = UserRepoAdapter(userDetails.gitRepos,applicationContext, token)
                        binding.userRepositoryList.layoutManager = LinearLayoutManager(applicationContext)
                        binding.userRepositoryList.visibility = View.VISIBLE
                    } else {

                        binding.userForest.visibility = View.GONE
                        binding.userRepos.visibility = View.GONE

                    }

                } else {
                    if (count < 4) {
                        count++
                        userDetailInfo()
                    }
                }
            //사용자 검색
            } else {
                val resultDeferred = coroutine.async(Dispatchers.IO) {
                    viewmodel.userDetails(githubId, token)
                }
                val userDetails = resultDeferred.await()

                // 실제 데이터 왔는지 확인    왔으면 깃허브 프로필 이미지, githubId, 사용자의 깃허브 랭킹 commit, issue pr, comment등을 보여줌
                if (userDetails.githubId != null && userDetails.commits != null && userDetails.issues != null && userDetails.profileImage != null &&
                    userDetails.pullRequests != null && userDetails.comments != null
                ) {
                    if (!this@UserDetailActivity.isFinishing) {
                        Glide.with(binding.githubProfile).load(userDetails.profileImage)
                            .into(binding.githubProfile)
                    }

                    binding.userId.text = userDetails.githubId
                    val chipCommit = Chip(applicationContext)
                    chipCommit.text = "Commit ${userDetails.commits}"

                    val chipIssues = Chip(applicationContext)
                    chipIssues.text = "Issues ${userDetails.issues}"

                    val chipPR = Chip(applicationContext)
                    chipPR.text = "PR ${userDetails.pullRequests}"

                    val chipComment = Chip(applicationContext)
                    chipComment.text = "Comments ${userDetails.comments}"

                    binding.userContribute.apply {
                        addView(chipCommit)
                        addView(chipIssues)
                        addView(chipPR)
                        addView(chipComment)
                    }
                    binding.userTier.text = userDetails.tier

                    if (userDetails.tokenAmount != null) {
                        when (userDetails.tokenAmount) {
                            0 -> {
                                binding.userArc.visibility = View.INVISIBLE
                            }
                            in 1..49 -> {
                                binding.progressStart.text = "1"
                                binding.progressEnd.text = "49"
                                binding.userArc.max = 49
                                binding.userArc.progress = userDetails.tokenAmount
                            }
                            in 50..199 -> {
                                binding.progressStart.text = "50"
                                binding.progressEnd.text = "199"
                                binding.userArc.max = 150
                                binding.userArc.progress = userDetails.tokenAmount - 50
                            }
                            in 200..499 -> {
                                binding.progressStart.text = "200"
                                binding.progressEnd.text = "499"
                                binding.userArc.max = 300
                                binding.userArc.progress = userDetails.tokenAmount - 200
                            }
                            in 500..1000 -> {
                                binding.progressStart.text = "500"
                                binding.progressEnd.text = "1000"
                                binding.userArc.max = 501
                                binding.userArc.progress = userDetails.tokenAmount - 500
                            }
                        }
                        binding.userArc.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                            override fun onProgressChanged(seekBar: SeekBar?, p1: Int, p2: Boolean) {
                                val value = seekBar?.progress
                                binding.currentProgress.text = userDetails.tokenAmount.toString()
                                if (value != 0) {
                                    val cur = seekBar!!.width / seekBar.max
                                    binding.currentProgress.x = (cur * value!!).toFloat() + 10
                                }
                                binding.currentProgress.y = seekBar?.pivotY!! + 10
                                Log.d(
                                    "Pos",
                                    binding.currentProgress.x.toString() + ": " + seekBar!!.width + ":" + seekBar.x
                                )
                            }
                            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
                        })
                    }
                    binding.userArc.isEnabled = false

                    // 조직 인증 완료시 속한 조직 이름과 전체 조직에서의 속한 조직의 랭킹 출력
                    if (userDetails.organization != null) {
                        binding.orgName.text = userDetails.organization
                        binding.orgRanking.text = userDetails.organizationRank.toString()
                    }

                    if (!userDetails.gitRepos.isNullOrEmpty()) {
                        binding.userRepositoryList.adapter = UserRepoAdapter(userDetails.gitRepos,applicationContext, token)
                        binding.userRepositoryList.layoutManager = LinearLayoutManager(applicationContext)
                        binding.userRepositoryList.visibility = View.VISIBLE
                    } else {

                        binding.userForest.visibility = View.GONE
                        binding.userRepos.visibility = View.GONE

                    }

                } else {
                    if (count < 4) {
                        count++
                        userDetailInfo()
                    }
                }
            }

        }

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