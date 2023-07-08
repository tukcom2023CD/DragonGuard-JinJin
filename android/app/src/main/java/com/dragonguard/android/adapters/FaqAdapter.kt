package com.dragonguard.android.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dragonguard.android.R
import com.dragonguard.android.databinding.FaqListBinding
import com.dragonguard.android.model.menu.FaqModel

class FaqAdapter(private val faqList: ArrayList<FaqModel>) : RecyclerView.Adapter<FaqAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FaqAdapter.ViewHolder {
        val binding = FaqListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = faqList.size

    inner class ViewHolder(private val binding: FaqListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int){
            binding.faqTitle.text = faqList[position].title
            binding.faqAnswer.text = faqList[position].content
            binding.faqTitle.setOnClickListener {
                if(faqList[position].expandable) {
                    binding.ctChild.visibility = View.GONE
                    faqList[position].expandable = false
                } else {
                    binding.ctChild.visibility = View.VISIBLE
                    faqList[position].expandable = true
                }
            }
        }

    }
    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(holder: FaqAdapter.ViewHolder, position: Int) {
        holder.bind(position)
    }
}