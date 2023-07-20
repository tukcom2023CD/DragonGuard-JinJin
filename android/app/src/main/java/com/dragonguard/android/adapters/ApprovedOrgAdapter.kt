package com.dragonguard.android.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dragonguard.android.databinding.ApprovedOrgListBinding
import com.dragonguard.android.model.org.ApproveRequestOrgModelItem

//승인된 조직 목록 adapter
class ApprovedOrgAdapter (private val datas : ArrayList<ApproveRequestOrgModelItem>, private val context: Context,
                          private val token: String) : RecyclerView.Adapter<ApprovedOrgAdapter.ViewHolder>() {
    private lateinit var binding: ApprovedOrgListBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ApprovedOrgListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root)
    }
    override fun getItemCount(): Int = datas.size

    //리사이클러 뷰의 요소들을 넣어줌
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(data1: ApproveRequestOrgModelItem) {
            binding.emailEndpoint.text = data1.email_endpoint
            binding.approvedOrgName.text = data1.name
            binding.approvedOrgType.text = data1.type
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