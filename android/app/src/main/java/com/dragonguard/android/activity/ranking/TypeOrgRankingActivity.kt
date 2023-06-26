package com.dragonguard.android.activity.ranking

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
import com.dragonguard.android.activity.MainActivity
import com.dragonguard.android.databinding.ActivityTypeOrgRankingBinding
import com.dragonguard.android.model.rankings.OrganizationRankingModel
import com.dragonguard.android.model.rankings.TotalOrganizationModel
import com.dragonguard.android.recycleradapter.TotalOrgRankingAdapter
import com.dragonguard.android.viewmodel.Viewmodel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

/*
 모든 대학교들의 랭킹을 보여주는 activity
 */
class TypeOrgRankingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTypeOrgRankingBinding
    private var viewmodel = Viewmodel()
    private var token = ""
    private var type = "COMPANY"
    private var page = 0
    private var position = 0
    private var changed = true
    private var ranking = 0
    private var totalOrgRankings = ArrayList<TotalOrganizationModel>()
    private lateinit var totalOrgAdapter : TotalOrgRankingAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_type_org_ranking)
        binding.typeOrganizationViewmodel = viewmodel

        setSupportActionBar(binding.toolbar) //커스텀한 toolbar를 액션바로 사용
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
        token = intent.getStringExtra("token")!!
        type = intent.getStringExtra("orgType")!!
        binding.orgTypeTitle.text = type
        typeOrgRankings()
    }

    private fun typeOrgRankings() {
        binding.progressBar.visibility = View.VISIBLE
        binding.orgNameType.text = type
        val coroutine = CoroutineScope(Dispatchers.Main)
        coroutine.launch {
            if(!this@TypeOrgRankingActivity.isFinishing) {
                val resultDeferred = coroutine.async(Dispatchers.IO) {
                    viewmodel.typeOrgRankings(type, page, token)
                }
                val result = resultDeferred.await()
                checkRankings(result)
            }
        }
    }

    private fun checkRankings(result: OrganizationRankingModel) {
        if (result.isNotEmpty()) {
            Log.d("조직 내 랭킹", "결과 : ${result[0].name}")
            result.forEach {
                Log.d("조직 내 랭킹", "결과 : ${it.name}")
                if (ranking != 0) {
                    if (totalOrgRankings[ranking - 1].token_sum == it.token_sum) {
                        totalOrgRankings.add(
                            TotalOrganizationModel(
                                it.email_endpoint, it.id, it.name, it.organization_type, it.token_sum,
                                totalOrgRankings[ranking - 1].ranking
                            )
                        )
                    } else {
                        totalOrgRankings.add(
                            TotalOrganizationModel(
                                it.email_endpoint,
                                it.id,
                                it.name,
                                it.organization_type,
                                it.token_sum,
                                ranking + 1
                            )
                        )
                    }
                } else {
                    totalOrgRankings.add(
                        TotalOrganizationModel(
                            it.email_endpoint,
                            it.id,
                            it.name,
                            it.organization_type,
                            it.token_sum,
                            1
                        )
                    )
                }
                Log.d("유져", "랭킹 ${ranking+1} 추가")
                ranking++
            }
            Log.d("뷰 보이기 전", "initrecycler 전")
            initRecycler()

        } else {
            if (changed) {
                changed = false
                val handler = Handler(Looper.getMainLooper())
                handler.postDelayed({ typeOrgRankings() }, 2000)
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    private fun initRecycler() {
        Log.d("recycler", "initrecycler()")
        binding.typeOrgRanking.setItemViewCacheSize(totalOrgRankings.size)
        if (page == 0) {
            totalOrgAdapter =
                TotalOrgRankingAdapter(totalOrgRankings, this)
            binding.typeOrgRanking.adapter = totalOrgAdapter
            binding.typeOrgRanking.layoutManager = LinearLayoutManager(this)
            binding.typeOrgRanking.visibility = View.VISIBLE
        }
        binding.typeOrgRanking.adapter?.notifyDataSetChanged()
        page++
        Log.d("api 횟수", "$page 페이지 검색")
        binding.progressBar.visibility = View.GONE
        initScrollListener()
    }

    private fun loadMorePosts() {
        if (binding.progressBar.visibility == View.GONE) {
            binding.progressBar.visibility = View.VISIBLE
            changed = true
            CoroutineScope(Dispatchers.Main).launch {
                Log.d("api 시도", "getTotalUsersRanking 실행  load more")
                typeOrgRankings()
            }
        }
    }

    private fun initScrollListener() {
        binding.typeOrgRanking.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = binding.typeOrgRanking.layoutManager
                val lastVisibleItem = (layoutManager as LinearLayoutManager)
                    .findLastCompletelyVisibleItemPosition()
                val itemTotalCount = recyclerView.adapter!!.itemCount - 1
                position = recyclerView.adapter!!.itemCount - 1
                // 마지막으로 보여진 아이템 position 이
                // 전체 아이템 개수보다 1개 모자란 경우, 데이터를 loadMore 한다
                if (!binding.typeOrgRanking.canScrollVertically(1) && lastVisibleItem == itemTotalCount) {
                    loadMorePosts()
                }
            }
        })
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