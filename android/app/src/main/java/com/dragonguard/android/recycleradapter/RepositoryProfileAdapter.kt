package com.dragonguard.android.recycleradapter

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.dragonguard.android.R

class RepositoryProfileAdapter (private val datas : ArrayList<String>,private val context: Context) : RecyclerView.Adapter<RepositoryProfileAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.repository_list,parent,false)
        return ViewHolder(view)
    }
    override fun getItemCount(): Int = datas.size

    //리사이클러 뷰의 요소들을 넣어줌
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val repoName: TextView = itemView.findViewById(R.id.repo_name)

        //클릭리스너 구현
        fun bind(data: String) {
            repoName.text = data
            itemView.setOnClickListener{
                Toast.makeText(context, "${repoName.text} 눌림", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datas[position])
    }

}