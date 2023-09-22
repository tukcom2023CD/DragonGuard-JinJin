package com.dragonguard.android.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.dragonguard.android.R
import com.dragonguard.android.activity.basic.MainActivity
import com.dragonguard.android.activity.profile.UserProfileActivity
import com.dragonguard.android.activity.ranking.MyOrganizationInternalActivity
import com.dragonguard.android.databinding.RankingListBinding
import com.dragonguard.android.model.rankings.*

class RankingsAdapter(private val rankings: List<*>, private val context: Context, private val token: String): RecyclerView.Adapter<RankingsAdapter.ViewHolder>()  {
    private lateinit var binding: RankingListBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RankingsAdapter.ViewHolder {
        binding = RankingListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(data1: Any?){
            binding.eachProfile.clipToOutline = true
            when(data1) {
                is TotalUsersRankingsModel -> {
                    binding.eachRanking.text = data1.ranking.toString()
                    Glide.with(binding.eachProfile).load(data1.profile_image)
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(binding.eachProfile)
                    binding.rankingGithubId.text = data1.github_id
                    binding.rankingContribute.text = data1.tokens.toString()
                    when(data1.tier) {
                        "BRONZE" -> {
                            binding.rankerContent.setBackgroundResource(R.drawable.shadow_bronze)
                        }
                        "SILVER" -> {
                            binding.rankerContent.setBackgroundResource(R.drawable.shadow_silver)
                        }
                        "GOLD" -> {
                            binding.rankerContent.setBackgroundResource(R.drawable.shadow_gold)
                        }
                        "PLATINUM" -> {
                            binding.rankerContent.setBackgroundResource(R.drawable.shadow_platinum)
                        }
                        "DIAMOND" -> {
                            binding.rankerContent.setBackgroundResource(R.drawable.shadow_diamond)
                        }
                        else -> {
                            binding.rankerContent.setBackgroundResource(R.drawable.shadow_unrank)
                        }
                    }
                    binding.rankingItem.setOnClickListener {
                        val mContext = context as MainActivity
                        val intent = Intent(context, UserProfileActivity::class.java)
                        intent.putExtra("userName", data1.github_id)
                        intent.putExtra("token", token)
                        context.startActivity(intent)
                    }
                }
                is OrgInternalRankingsModel -> {
                    Log.d("image", "profile_img : ${data1.profile_image}")
                    if(data1.profile_image.isNullOrBlank()) {
                        binding.profileLink.visibility = View.GONE
                    } else {
                        binding.profileLink.visibility = View.VISIBLE
                    }

                    binding.eachRanking.text = data1.ranking.toString()
                    Glide.with(binding.eachProfile).load(data1.profile_image)
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(binding.eachProfile)
                    binding.rankingGithubId.text = data1.github_id
                    binding.rankingContribute.text = data1.tokens.toString()
                    binding.rankingItem.setOnClickListener {
                        if(data1.profile_image.isNullOrBlank()) {
                            val intent = Intent(context, MyOrganizationInternalActivity::class.java)
                            intent.putExtra("organization", data1.name)
                            intent.putExtra("token", token)
                            context.startActivity(intent)
                        } else {
                            val intent = Intent(context, UserProfileActivity::class.java)
                            intent.putExtra("userName", data1.github_id)
                            intent.putExtra("token", token)
                            context.startActivity(intent)
                        }
                    }
                    when(data1.tier) {
                        "BRONZE" -> {
                            binding.rankerContent.setBackgroundResource(R.drawable.shadow_bronze)
                        }
                        "SILVER" -> {
                            binding.rankerContent.setBackgroundResource(R.drawable.shadow_silver)
                        }
                        "GOLD" -> {
                            binding.rankerContent.setBackgroundResource(R.drawable.shadow_gold)
                        }
                        "PLATINUM" -> {
                            binding.rankerContent.setBackgroundResource(R.drawable.shadow_platinum)
                        }
                        "DIAMOND" -> {
                            binding.rankerContent.setBackgroundResource(R.drawable.shadow_diamond)
                        }
                        else -> {
                            binding.rankerContent.setBackgroundResource(R.drawable.shadow_unrank)
                        }
                    }
                }
                is TotalOrganizationModel -> {
                    binding.profileLink.visibility = View.GONE
                    binding.eachRanking.text = data1.ranking.toString()
                    binding.rankingGithubId.text = data1.name
                    when(data1.organization_type) {
                        "COMPANY" -> {
                            binding.eachProfile.setImageResource(R.drawable.company)
                        }
                        "UNIVERSITY" -> {
                            binding.eachProfile.setImageResource(R.drawable.university)
                        }
                        "HIGH_SCHOOL" -> {
                            binding.eachProfile.setImageResource(R.drawable.high_school)
                        }
                        else -> {

                        }
                    }
                    binding.rankingContribute.text = data1.token_sum.toString()
                }
            }
        }

    }
    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int = rankings.size

    override fun onBindViewHolder(holder: RankingsAdapter.ViewHolder, position: Int) {
        holder.bind(rankings[position])
    }
}