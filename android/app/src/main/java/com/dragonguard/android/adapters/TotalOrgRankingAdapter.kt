package com.dragonguard.android.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dragonguard.android.databinding.TotalUsersRankingListBinding
import com.dragonguard.android.model.rankings.TotalOrganizationModel

class TotalOrgRankingAdapter(private val datas : ArrayList<TotalOrganizationModel>, private val context: Context) : RecyclerView.Adapter<TotalOrgRankingAdapter.ViewHolder>() {
    private lateinit var binding: TotalUsersRankingListBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = TotalUsersRankingListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root)
    }
    override fun getItemCount(): Int = datas.size

    //리사이클러 뷰의 요소들을 넣어줌
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(data: TotalOrganizationModel) {
            if(binding.totalUsersRanking.text.isNullOrEmpty() && binding.rankerId.text.isNullOrEmpty() && binding.rankerContribution.text.isNullOrEmpty()) {
                binding.totalUsersRanking.text = data.ranking.toString()
                binding.rankerId.text = data.name
                if(data.token_sum == null) {
                    binding.rankerContribution.text = "NONE"
                } else {
                    binding.rankerContribution.text = data.token_sum.toString()
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
        return position
    }

}