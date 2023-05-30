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
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.dragonguard.android.R
import com.dragonguard.android.activity.LoginActivity
import com.dragonguard.android.activity.MainActivity
import com.dragonguard.android.connect.NetworkCheck
import com.dragonguard.android.databinding.FragmentMainBinding
import com.dragonguard.android.model.UserInfoModel
import com.dragonguard.android.viewmodel.Viewmodel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
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
        drawInfo()
        CoroutineScope(Dispatchers.IO).launch{
            while(true){
                Thread.sleep(3000)
                handler.sendEmptyMessage(0)
            }
        }
    }

    private fun drawInfo() {
        if (info.githubId!!.isNotBlank()) {
            binding.userId.text = info.githubId
        }
        if(!requireActivity().isFinishing) {
            Glide.with(binding.githubProfile).load(info.profileImage)
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
            "DIAMOND" ->{
                binding.tierImg.setBackgroundResource(R.drawable.diamond)
            }
        }

        if (info.tokenAmount != null) {
            binding.tokenAmount.text = info.tokenAmount.toString()
        }
        val typeList = listOf("commits", "issues", "pullRequests", "review")
        binding.userOrgName.text = info.organization!!
        val userActivity = HashMap<String, Int>()
        userActivity.put("commits", info.commits!!)
        userActivity.put("issues", info.issues!!)
        userActivity.put("pullRequests", info.pullRequests!!)
        userActivity.put("review", info.review!!)
    }

    private fun setPage(){
        binding.userUtil.setCurrentItem((binding.userUtil.currentItem+1)%4,false)
    }




}