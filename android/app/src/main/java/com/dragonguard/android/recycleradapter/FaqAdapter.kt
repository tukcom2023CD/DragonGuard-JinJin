package com.dragonguard.android.recycleradapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dragonguard.android.R
import com.dragonguard.android.model.menu.FaqModel

class FaqAdapter(private val faqList: ArrayList<FaqModel>) : RecyclerView.Adapter<FaqAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FaqAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.faq_list,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = faqList.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val title = view.findViewById<TextView>(R.id.faq_title)
        private val answer = view.findViewById<TextView>(R.id.faq_answer)
        private val dropdown = view.findViewById<ImageView>(R.id.faq_dropdown)
        private val parent = view.findViewById<LinearLayout>(R.id.ct_parent)
        private val child = view.findViewById<LinearLayout>(R.id.ct_child)
        fun bind(position: Int){
            title.text = faqList[position].title
            answer.text = faqList[position].content
            parent.setOnClickListener {
                if(faqList[position].expandable) {
                    child.visibility = View.GONE
                    dropdown.setImageResource(R.drawable.ic_baseline_arrow_drop_down_24)
                    faqList[position].expandable = false
                } else {
                    child.visibility = View.VISIBLE
                    dropdown.setImageResource(R.drawable.baseline_arrow_right_24)
                    faqList[position].expandable = true
                }
            }
        }

    }

    override fun onBindViewHolder(holder: FaqAdapter.ViewHolder, position: Int) {
        holder.bind(position)
    }
}