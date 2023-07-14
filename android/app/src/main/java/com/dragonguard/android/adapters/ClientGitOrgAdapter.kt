package com.dragonguard.android.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.dragonguard.android.activity.profile.ClientReposActivity
import com.dragonguard.android.activity.search.RepoContributorsActivity
import com.dragonguard.android.databinding.GitOrganizationListBinding
import com.dragonguard.android.model.detail.GitOrganization

class ClientGitOrgAdapter (private val datas : List<GitOrganization>, private val context: Context,
                           private val token: String)
    : RecyclerView.Adapter<ClientGitOrgAdapter.ViewHolder>() {
    private lateinit var binding: GitOrganizationListBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = GitOrganizationListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root)
    }
    override fun getItemCount(): Int = datas.size

    //리사이클러 뷰의 요소들을 넣어줌
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        //클릭리스너 구현
        fun bind(data: GitOrganization) {
            Glide.with(binding.gitOrganizationProfile).load(data.profile_image)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(binding.gitOrganizationProfile)

            binding.gitOrganizationName.text = data.name
            binding.gitOrgImg.setOnClickListener {
                Intent(context, ClientReposActivity::class.java).apply{
                    putExtra("orgName", data.name)
                    putExtra("token", token)
                    putExtra("img", data.profile_image)
                }.run{context.startActivity(this)}
            }
        }
    }

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }
    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datas[position])
    }

}