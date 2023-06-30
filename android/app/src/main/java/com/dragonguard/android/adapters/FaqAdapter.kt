package com.dragonguard.android.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dragonguard.android.R
import com.dragonguard.android.databinding.FaqListBinding
import com.dragonguard.android.model.menu.FaqModel

class FaqAdapter(private val faqList: ArrayList<FaqModel>) : RecyclerView.Adapter<FaqAdapter.ViewHolder>() {
    private lateinit var binding: FaqListBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FaqAdapter.ViewHolder {
        binding = FaqListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root)
    }

    override fun getItemCount(): Int = faqList.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(position: Int){
            binding.faqTitle.text = faqList[position].title
            binding.faqAnswer.text = faqList[position].content
            binding.ctParent.setOnClickListener {
                if(faqList[position].expandable) {
                    binding.ctChild.visibility = View.GONE
                    binding.faqDropdown.setImageResource(R.drawable.ic_baseline_arrow_drop_down_24)
                    faqList[position].expandable = false
                } else {
                    binding.ctChild.visibility = View.VISIBLE
                    binding.faqDropdown.setImageResource(R.drawable.baseline_arrow_right_24)
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