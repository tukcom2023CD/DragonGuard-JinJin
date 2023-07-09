package com.dragonguard.android.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dragonguard.android.R
import com.dragonguard.android.adapters.RankingsAdapter
import com.dragonguard.android.databinding.FragmentAllRankingsBinding
import com.dragonguard.android.databinding.FragmentTotalOrganizationBinding
import com.dragonguard.android.model.rankings.OrganizationRankingModel
import com.dragonguard.android.model.rankings.TotalOrganizationModel
import com.dragonguard.android.viewmodel.Viewmodel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class TotalOrganizationFragment(private val token: String) : Fragment() {
    private lateinit var binding: FragmentTotalOrganizationBinding
    private var viewmodel = Viewmodel()
    private var page = 0
    private var position = 0
    private var ranking = 0
    private var changed = true
    private lateinit var rankingsAdapter: RankingsAdapter
    private var totalOrgRankings = ArrayList<TotalOrganizationModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTotalOrganizationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        typeOrgRankings()
    }
    private fun typeOrgRankings() {
        binding.rankingLottie.visibility = View.VISIBLE
        binding.rankingLottie.playAnimation()
        val coroutine = CoroutineScope(Dispatchers.Main)
        coroutine.launch {
            if(!this@TotalOrganizationFragment.isRemoving) {
                val resultDeferred = coroutine.async(Dispatchers.IO) {
                    viewmodel.totalOrgRankings(page, token)
                }
                val result = resultDeferred.await()
                checkRankings(result)
            }
        }
    }

    private fun checkRankings(result: OrganizationRankingModel) {
        Log.d("전체", "전체 조직들: $result")
        if (result.isNotEmpty()) {
            result.forEach {
                Log.d("조직 내 랭킹", "결과 : ${it.name}")
                if (ranking != 0) {
                    if (totalOrgRankings[ranking - 1].token_sum == it.token_sum) {
                        totalOrgRankings.add(
                            TotalOrganizationModel(it.email_endpoint, it.id, it.name, it.organization_type, it.token_sum,
                                totalOrgRankings[ranking - 1].ranking)
                        )
                    } else {
                        totalOrgRankings.add(
                            TotalOrganizationModel(it.email_endpoint, it.id, it.name, it.organization_type, it.token_sum, ranking + 1)
                        )
                    }
                } else {
                    totalOrgRankings.add(
                        TotalOrganizationModel(it.email_endpoint, it.id, it.name, it.organization_type, it.token_sum, 1)
                    )
                }
                Log.d("유져", "랭킹 ${ranking+1} 추가")
                ranking++
            }
            Log.d("뷰 보이기 전", "initrecycler 전")
            initOrgRecycler()

        } else {
            if (changed) {
                changed = false
                val handler = Handler(Looper.getMainLooper())
                handler.postDelayed({ typeOrgRankings() }, 2000)
            } else {
                binding.rankingLottie.pauseAnimation()
                binding.rankingLottie.visibility = View.GONE
            }
        }
    }

    private fun initOrgRecycler() {
        if(page == 0) {
            when( totalOrgRankings.size) {
                1 -> {
                    profileOrgBackground(totalOrgRankings[0], 1)
                }
                2 -> {
                    profileOrgBackground(totalOrgRankings[0], 1)
                    profileOrgBackground(totalOrgRankings[1], 2)
                }
                3 -> {
                    profileOrgBackground(totalOrgRankings[0], 1)
                    profileOrgBackground(totalOrgRankings[1], 2)
                    profileOrgBackground(totalOrgRankings[2], 3)
                }
                else -> {
                    profileOrgBackground(totalOrgRankings[0], 1)
                    profileOrgBackground(totalOrgRankings[1], 2)
                    profileOrgBackground(totalOrgRankings[2], 3)

                    totalOrgRankings.removeFirst()
                    totalOrgRankings.removeFirst()
                    totalOrgRankings.removeFirst()

                    rankingsAdapter = RankingsAdapter(totalOrgRankings)
                    binding.eachRankings.adapter = rankingsAdapter
                    binding.eachRankings.layoutManager = LinearLayoutManager(requireContext())
//            totalUserRankingAdapter.notifyDataSetChanged()
                    binding.eachRankings.visibility = View.VISIBLE
                }
            }
        }
        binding.eachRankings.adapter?.notifyDataSetChanged()
        page++
        Log.d("api 횟수", "$page 페이지 검색")
        binding.rankingLottie.pauseAnimation()
        binding.rankingLottie.visibility = View.GONE
        binding.topRankings.visibility = View.VISIBLE
        initOrgScrollListener()

    }

    private fun profileOrgBackground(model: TotalOrganizationModel, number: Int) {
        when(number) {
            1-> {
//                Glide.with(binding.firstProfile).load()
//                    .into(binding.firstProfile)
                binding.firstId.text = model.name
                binding.firstContribute.text = model.token_sum.toString()
                binding.firstRanker.visibility = View.VISIBLE
                binding.firstProfile.setImageResource(profileOrg(model))
            }
            2 -> {
                binding.secondId.text = model.name
                binding.secondContribute.text = model.token_sum.toString()
                binding.secondRanker.visibility = View.VISIBLE
                binding.secondProfile.setImageResource(profileOrg(model))
            }
            3 -> {
                binding.thirdId.text = model.name
                binding.thirdContribute.text = model.token_sum.toString()
                binding.thirdRanker.visibility = View.VISIBLE
                binding.thirdProfile.setImageResource(profileOrg(model))
            }
        }
    }

    private fun profileOrg(model: TotalOrganizationModel): Int {
        return when(model.organization_type) {
            "UNIVERSITY" -> R.drawable.university
            "COMPANY" -> R.drawable.company
            "HIGH_SCHOOL" -> R.drawable.high_school
            "ETC" -> R.drawable.more
            else -> R.drawable.more
        }
    }

    private fun loadOrgMorePosts() {
        if (binding.rankingLottie.visibility == View.GONE) {
            binding.rankingLottie.visibility = View.VISIBLE
            binding.rankingLottie.playAnimation()
            changed = true
            CoroutineScope(Dispatchers.Main).launch {
                Log.d("api 시도", "getTotalUsersRanking 실행  load more")
                typeOrgRankings()
            }
        }
    }

    private fun initOrgScrollListener() {
        binding.eachRankings.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = binding.eachRankings.layoutManager
                val lastVisibleItem = (layoutManager as LinearLayoutManager)
                    .findLastCompletelyVisibleItemPosition()
                val itemTotalCount = recyclerView.adapter!!.itemCount - 1
                position = recyclerView.adapter!!.itemCount - 1
                // 마지막으로 보여진 아이템 position 이
                // 전체 아이템 개수보다 1개 모자란 경우, 데이터를 loadMore 한다
                if (!binding.eachRankings.canScrollVertically(1) && lastVisibleItem == itemTotalCount) {
                    loadOrgMorePosts()
                }
            }
        })
    }
}