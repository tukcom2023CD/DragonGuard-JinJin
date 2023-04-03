package com.dragonguard.android.recycleradapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dragonguard.android.R
import com.dragonguard.android.model.rankings.OrgInternalRankingsModel

class OrgInternalRankingAdapter (private val datas : ArrayList<OrgInternalRankingsModel>, private val context: Context) : RecyclerView.Adapter<OrgInternalRankingAdapter.ViewHolder>() {

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

        fun bind(data: OrgInternalRankingsModel) {
            if(ranking.text.isNullOrEmpty() && githubId.text.isNullOrEmpty() && contribution.text.isNullOrEmpty()) {
                ranking.text = data.ranking.toString()
                githubId.text = data.githubId
//                Toast.makeText(context, "${data.tokens}", Toast.LENGTH_SHORT).show()
                if(data.tokens == null) {
                    contribution.text = "NONE"
                } else {
                    contribution.text = data.tokens.toString()
                }
            }
        }
//        fun bind(data1: TotalUsersRankingsModel, data2: TotalUsersRankingsModel) {
//            if(ranking.text.isNullOrEmpty() && githubId.text.isNullOrEmpty() && contribution.text.isNullOrEmpty()) {
//                if(data1.commits == data2.commits) {
//                    ranking.text = data2.ranking.toString()
//                } else {
//                    ranking.text = data1.ranking.toString()
//                }
//                githubId.text = data1.githubId
//                contribution.text = data1.commits.toString()
//            }
//        }
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