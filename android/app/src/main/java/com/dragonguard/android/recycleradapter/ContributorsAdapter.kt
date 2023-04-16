package com.dragonguard.android.recycleradapter

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.dragonguard.android.R
import com.dragonguard.android.activity.UserDetailActivity
import com.dragonguard.android.model.contributors.RepoContributorsItem

/*
 선택한 repo의 contributor들의 정보를 나열하기 위한 recycleradapter
 */
class ContributorsAdapter (private val datas : ArrayList<RepoContributorsItem>, private val context: Context, private val colors: ArrayList<Int>,
                           private val token: String, private val repoName: String) : RecyclerView.Adapter<ContributorsAdapter.ViewHolder>() {
    private var ranking  = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.contributors_list,parent,false)
        return ViewHolder(view)
    }
    override fun getItemCount(): Int = datas.size

    //리사이클러 뷰의 요소들을 넣어줌
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val contRanking: TextView = itemView.findViewById(R.id.contribute_ranking)
        private val githubId : TextView = itemView.findViewById(R.id.contrubutor_id)
        private val color : ImageView = itemView.findViewById(R.id.contributor_color)
        private val layout: ConstraintLayout = itemView.findViewById(R.id.contributors_layout)

        fun bind(data1: RepoContributorsItem) {
            contRanking.text = data1.commits.toString()
            githubId.text = data1.githubId
            val red = (Math.random()*255).toInt()
            val green = (Math.random()*255).toInt()
            val blue = (Math.random()*255).toInt()
            color.imageTintList = ColorStateList.valueOf(Color.rgb(red,green,blue))
            colors.add(Color.rgb(red,green,blue))
            layout.setOnClickListener {
                Intent(context, UserDetailActivity::class.java).apply{
                    putExtra("githubId", data1.githubId)
                    putExtra("token", token)
                }.run{context.startActivity(this)}
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
        return super.getItemViewType(position)
    }
    
}