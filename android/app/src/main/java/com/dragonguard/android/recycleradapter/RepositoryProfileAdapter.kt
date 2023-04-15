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
import com.dragonguard.android.UserDetailActivity
import com.dragonguard.android.activity.search.RepoContributorsActivity
import com.dragonguard.android.model.search.RepoSearchResultModel

//검색한 레포지토리 나열하는 리사이클러뷰 어댑터 구현
class RepositoryProfileAdapter (private val datas : ArrayList<RepoSearchResultModel>, private val context: Context,
                                private val token: String, private val type: String) : RecyclerView.Adapter<RepositoryProfileAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.repository_list,parent,false)
        return ViewHolder(view)
    }
    override fun getItemCount(): Int = datas.size

    //리사이클러 뷰의 요소들을 넣어줌
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val repoName: TextView = itemView.findViewById(R.id.repo_name)

        //클릭리스너 구현
        fun bind(data: RepoSearchResultModel) {
            repoName.text = data.name
            Log.d("name", "$data.name")
            itemView.setOnClickListener{
//                Toast.makeText(context, "${repoName.text} 눌림", Toast.LENGTH_SHORT).show()
                if(type == "USERS") {
                    Intent(context, UserDetailActivity::class.java).apply{
                        putExtra("githubId", data.id)
                        putExtra("token", token)
                    }.run{context.startActivity(this)}
                } else {
                    Intent(context, RepoContributorsActivity::class.java).apply{
                        putExtra("repoName", data.name)
                        putExtra("token", token)
                    }.run{context.startActivity(this)}
                }
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datas[position])
    }

}