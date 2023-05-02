package com.dragonguard.android.recycleradapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dragonguard.android.databinding.ApproveRequestListBinding
import com.dragonguard.android.model.contributors.RepoContributorsItem

class ApproveRequestOrgAdapter (private val datas : ArrayList<RepoContributorsItem>, private val context: Context,
                                private val token: String) : RecyclerView.Adapter<ApproveRequestOrgAdapter.ViewHolder>() {
    private lateinit var binding: ApproveRequestListBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ApproveRequestListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root)
    }
    override fun getItemCount(): Int = datas.size

    //리사이클러 뷰의 요소들을 넣어줌
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(data1: RepoContributorsItem) {
            binding.approveOrgBtn.setOnClickListener {

            }
            binding.requestOrgName.setOnClickListener {

            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datas[position])
    }
    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }
    override fun getItemViewType(position: Int): Int {
        return position
    }

}