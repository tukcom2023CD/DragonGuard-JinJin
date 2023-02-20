package com.dragonguard.android.recycleradapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dragonguard.android.R
import com.dragonguard.android.activity.CompareSearchActivity
import com.dragonguard.android.activity.MainActivity
import com.dragonguard.android.activity.RepoContributorsActivity
import com.dragonguard.android.model.RepoSearchResultModel

class CompareRepositoryAdapter (private val datas : ArrayList<RepoSearchResultModel>, private val context: Context, count: Int) : RecyclerView.Adapter<CompareRepositoryAdapter.ViewHolder>() {
    private var repoCount = count
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.repository_list,parent,false)
        return ViewHolder(view)
    }
    override fun getItemCount(): Int = datas.size

    //리사이클러 뷰의 요소들을 넣어줌
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val repoName: TextView = itemView.findViewById(R.id.repo_name)

        //클릭리스너 구현
        fun bind(data: RepoSearchResultModel, count: Int) {
            val mContext = context as CompareSearchActivity
            repoName.text = data.name
            Log.d("name", "$data.name")
            itemView.setOnClickListener{
                val intent = Intent(context, MainActivity::class.java)
                intent.putExtra("repo_name", data.name)
                context.setResult(count, intent)
                context.finish()
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datas[position], repoCount)
    }

}