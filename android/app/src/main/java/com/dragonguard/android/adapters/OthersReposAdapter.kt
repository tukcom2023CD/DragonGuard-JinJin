package com.dragonguard.android.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.dragonguard.android.activity.search.RepoContributorsActivity
import com.dragonguard.android.databinding.OthersReposListBinding

class OthersReposAdapter (private val datas : List<String>, private val context: Context,
                          private val token: String, private val img: String, private val userName: String)
    : RecyclerView.Adapter<OthersReposAdapter.ViewHolder>() {
    private lateinit var binding: OthersReposListBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = OthersReposListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root)
    }
    override fun getItemCount(): Int = datas.size

    //리사이클러 뷰의 요소들을 넣어줌
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        //클릭리스너 구현
        fun bind(data: String) {
            binding.reposFrame.clipToOutline = true
            binding.repoName.text = data
            Glide.with(binding.othersProfileImg).load(img)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(binding.othersProfileImg)
            binding.othersProfileImg.clipToOutline = true
            binding.userName.text = userName
            binding.repoContributeImg.setOnClickListener {
                Intent(context, RepoContributorsActivity::class.java).apply{
                    putExtra("repoName", data)
                    putExtra("token", token)
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