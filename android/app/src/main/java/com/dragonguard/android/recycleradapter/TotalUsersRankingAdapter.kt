package com.dragonguard.android.recycleradapter

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dragonguard.android.R
import com.dragonguard.android.model.RepoContributorsItem
import com.dragonguard.android.model.TotalUsersRankingModelItem

/*
 모든 사용자들을 랭킹 순서대로 나열하기 위한 recycleradapter
 */
class TotalUsersRankingAdapter (private val datas : ArrayList<TotalUsersRankingModelItem>, private val context: Context) : RecyclerView.Adapter<TotalUsersRankingAdapter.ViewHolder>() {
    private var rank = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.total_users_ranking_list,parent,false)
        return ViewHolder(view)
    }
    override fun getItemCount(): Int = datas.size

    //리사이클러 뷰의 요소들을 넣어줌
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val ranking: TextView = itemView.findViewById(R.id.total_users_ranking)
        private val githubId : TextView = itemView.findViewById(R.id.ranker_id)
        private val contribution : TextView = itemView.findViewById(R.id.ranker_contribution)

        fun bind(data: TotalUsersRankingModelItem) {
            if(ranking.text.isNullOrEmpty()) {
                ranking.text = rank.toString()
                rank++
            }
            githubId.text = data.githubId
            contribution.text = data.commits.toString()
        }
        fun bind(data1: TotalUsersRankingModelItem, data2: TotalUsersRankingModelItem) {
            if(ranking.text.isNullOrEmpty()) {
                if(data1.commits == data2.commits) {
                    ranking.text = (rank-1).toString()
                } else {
                    ranking.text = rank.toString()
                }
                rank++
            }
            githubId.text = data1.githubId
            contribution.text = data1.commits.toString()
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(position != 0) {
            holder.bind(datas[position], datas[position-1])
        } else {
            holder.bind(datas[position])
        }

    }

}