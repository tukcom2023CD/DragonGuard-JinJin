package com.dragonguard.android.fragment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.dragonguard.android.R
import com.dragonguard.android.activity.TokenHistoryActivity
import com.dragonguard.android.activity.search.SearchActivity
import com.dragonguard.android.databinding.FragmentMainBinding
import com.dragonguard.android.model.UserInfoModel
import com.dragonguard.android.recycleradapter.UserActivityAdapter
import com.dragonguard.android.viewmodel.Viewmodel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainFragment(private val token: String, private val info: UserInfoModel) : Fragment() {
    private lateinit var binding: FragmentMainBinding
    private var viewmodel = Viewmodel()
    private var count = 0
    private var state = true
    private var loginOut = false
    val handler= Handler(Looper.getMainLooper()){
        setPage()
        true
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        binding.mainFragViewmodel = viewmodel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tokenFrame.setOnClickListener {
            val intent = Intent(requireActivity(), TokenHistoryActivity::class.java)
            intent.putExtra("token", token)
            startActivity(intent)
        }

        drawInfo()
        CoroutineScope(Dispatchers.IO).launch{
            while(true){
                Thread.sleep(3000)
                handler.sendEmptyMessage(0)
            }
        }
    }

    private fun drawInfo() {
        if (info.github_id!!.isNotBlank()) {
            binding.userId.text = info.github_id
        }
        if(!requireActivity().isFinishing) {
            Glide.with(binding.githubProfile).load(info.profile_image)
                .into(binding.githubProfile)
        }

        when(info.tier) {
            "BRONZE" ->{
                binding.tierImg.setBackgroundResource(R.drawable.bronze)
            }
            "SILVER" ->{
                binding.tierImg.setBackgroundResource(R.drawable.silver)
            }
            "GOLD" ->{
                binding.tierImg.setBackgroundResource(R.drawable.gold)
            }
            "PLATINUM" ->{
                binding.tierImg.setBackgroundResource(R.drawable.platinum)
            }
            "DIAMOND" ->{
                binding.tierImg.setBackgroundResource(R.drawable.diamond)
            }
        }
        viewmodel.onSearchClickListener.observe(requireActivity(), Observer {
            if(viewmodel.onSearchClickListener.value == true) {
                val intent = Intent(requireActivity(), SearchActivity::class.java)
                intent.putExtra("token", token)
                startActivity(intent)
            }
        } )
        if (info.token_amount != null) {
            binding.tokenAmount.text = info.token_amount.toString()
        }
        val typeList = listOf("commits", "issues", "pullRequests", "review")
        if(info.organization != null) {
            binding.userOrgName.text = info.organization
        }
        val userActivity = HashMap<String, Int>()
        userActivity.put("commits", info.commits!!)
        userActivity.put("issues", info.issues!!)
        userActivity.put("pullRequests", info.pull_requests!!)
        info.reviews?.let {
            userActivity.put("review", it)
        }
        Log.d("map", "hashMap: $userActivity")
        binding.userUtil.adapter = UserActivityAdapter(userActivity, typeList, requireContext())
        binding.userUtil.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        if(info.organization == null) {
            binding.mainOrgFrame.visibility = View.GONE
        } else {
            when(info.organization_rank) {
                1 -> {
                    when(info.member_github_ids?.size){
                        1 -> {
                            binding.user1Githubid.text = info.member_github_ids!![0]
                            binding.user1Ranking.text = "1"
                        }
                        2 -> {
                            binding.user1Githubid.text = info.member_github_ids!![0]
                            binding.user1Ranking.text = "1"
                            binding.user2Githubid.text = info.member_github_ids!![1]
                            binding.user2Ranking.text = "2"
                        }
                        3 -> {
                            binding.user1Githubid.text = info.member_github_ids!![0]
                            binding.user1Ranking.text = "1"
                            binding.user2Githubid.text = info.member_github_ids!![1]
                            binding.user2Ranking.text = "2"
                            binding.user3Githubid.text = info.member_github_ids!![2]
                            binding.user3Ranking.text = "3"
                        }
                    }
                }
                else -> {
                    when(info.member_github_ids?.size){
                        2 -> {
                            binding.user1Githubid.text = info.member_github_ids!![0]
                            binding.user1Ranking.text = info.organization_rank!!.minus(1).toString()
                            binding.user2Githubid.text = info.member_github_ids!![1]
                            binding.user2Ranking.text = info.organization_rank!!.toString()
                        }
                        3 -> {
                            when(info.is_last) {
                                true -> {
                                    binding.user1Githubid.text = info.member_github_ids!![0]
                                    binding.user1Ranking.text = info.organization_rank!!.minus(2).toString()
                                    binding.user2Githubid.text = info.member_github_ids!![1]
                                    binding.user2Ranking.text = info.organization_rank!!.minus(1).toString()
                                    binding.user3Githubid.text = info.member_github_ids!![2]
                                    binding.user3Ranking.text = info.organization_rank!!.toString()
                                }
                                false -> {
                                    binding.user1Githubid.text = info.member_github_ids!![0]
                                    binding.user1Ranking.text = info.organization_rank!!.minus(1).toString()
                                    binding.user2Githubid.text = info.member_github_ids!![1]
                                    binding.user2Ranking.text = info.organization_rank!!.toString()
                                    binding.user3Githubid.text = info.member_github_ids!![2]
                                    binding.user3Ranking.text = info.organization_rank!!.plus(1).toString()
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun setPage(){
        binding.userUtil.setCurrentItem((binding.userUtil.currentItem+1)%4,false)
    }

}