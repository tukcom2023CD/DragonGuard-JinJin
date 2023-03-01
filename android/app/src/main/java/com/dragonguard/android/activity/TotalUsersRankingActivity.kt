package com.dragonguard.android.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dragonguard.android.R
import com.dragonguard.android.databinding.ActivityTotalUsersRankingBinding
import com.dragonguard.android.model.TotalUsersRankingsModel
import com.dragonguard.android.model.TotalUsersRankingModelItem
import com.dragonguard.android.recycleradapter.TotalUsersRankingAdapter
import com.dragonguard.android.viewmodel.Viewmodel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

/*
 가입한 모든 사용자들의 랭킹을 보여주는 activity
 */
class TotalUsersRankingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTotalUsersRankingBinding
    private lateinit var totalUserRankingAdapter: TotalUsersRankingAdapter
    private var viewmodel = Viewmodel()
    private val size = 20
    private var page = 0
    private var position = 0
    private var ranking = 0
    private var usersRanking = ArrayList<TotalUsersRankingsModel>()
    private var changed = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_total_users_ranking)
        binding.totalUsersRankingViewModel = viewmodel

        setSupportActionBar(binding.toolbar) //커스텀한 toolbar를 액션바로 사용
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)

        getTotalUsersRanking(page, size)
    }

    private fun getTotalUsersRanking(page: Int, size: Int) {
        binding.progressBar.visibility = View.VISIBLE
        val coroutine = CoroutineScope(Dispatchers.Main)
        coroutine.launch {
            val resultDeferred = coroutine.async(Dispatchers.IO) {
                viewmodel.getTotalUserRanking(page, size)
            }
            val result = resultDeferred.await()
            checkRankings(result)
        }
    }

    private fun checkRankings(result: ArrayList<TotalUsersRankingModelItem>) {
//        Toast.makeText(applicationContext, "개수 : ${result.size}",Toast.LENGTH_SHORT).show()
        if(result.isNotEmpty()) {
            result.forEach {
                if(it.tokens != null) {
                    if(ranking != 0) {
                        if(usersRanking[ranking-1].tokens == it.tokens) {
                            usersRanking.add(TotalUsersRankingsModel(it.tokens,it.githubId,it.id,it.name,it.tier,usersRanking[ranking-1].ranking))
                        } else {
                            usersRanking.add(TotalUsersRankingsModel(it.tokens,it.githubId,it.id,it.name,it.tier,ranking+1))
                        }
                    } else {
                        usersRanking.add(TotalUsersRankingsModel(it.tokens,it.githubId,it.id,it.name,it.tier,1))
                    }
//                Log.d("유져", "랭킹 ${ranking+1} 추가")
                    ranking++
                }
            }
            initRecycler()
        } else {
            if(changed) {
                changed = false
                val handler = Handler(Looper.getMainLooper())
                handler.postDelayed({getTotalUsersRanking(page, size)}, 4000)
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    private fun initRecycler() {
        binding.totalUsersRankings.setItemViewCacheSize(usersRanking.size)
//        Toast.makeText(applicationContext, "개수 : ${usersRanking.size}",Toast.LENGTH_SHORT).show()
        if(page == 0) {
            totalUserRankingAdapter = TotalUsersRankingAdapter(usersRanking, this)
            binding.totalUsersRankings.adapter = totalUserRankingAdapter
            binding.totalUsersRankings.layoutManager = LinearLayoutManager(this)
//            totalUserRankingAdapter.notifyDataSetChanged()
            binding.totalUsersRankings.visibility = View.VISIBLE
        }
        page++
        Log.d("api 횟수", "$page 페이지 검색")
        binding.progressBar.visibility = View.GONE
        initScrollListener()
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

    private fun loadMorePosts() {
        if (binding.progressBar.visibility == View.GONE) {
            binding.progressBar.visibility = View.VISIBLE
            changed = true
            CoroutineScope(Dispatchers.Main).launch {
                Log.d("api 시도", "getTotalUsersRanking 실행  load more")
                getTotalUsersRanking(page, size)
            }
        }
    }

    private fun initScrollListener() {
        binding.totalUsersRankings.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = binding.totalUsersRankings.layoutManager
                val lastVisibleItem = (layoutManager as LinearLayoutManager)
                    .findLastCompletelyVisibleItemPosition()
                val itemTotalCount = recyclerView.adapter!!.itemCount - 1
                position = recyclerView.adapter!!.itemCount - 1
                // 마지막으로 보여진 아이템 position 이
                // 전체 아이템 개수보다 1개 모자란 경우, 데이터를 loadMore 한다
                if (!binding.totalUsersRankings.canScrollVertically(1) && lastVisibleItem == itemTotalCount) {
                    loadMorePosts()
                }
            }
        })
    }
}