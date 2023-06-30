package com.dragonguard.android.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dragonguard.android.activity.search.RepoContributorsActivity
import com.dragonguard.android.databinding.TotalUsersRankingListBinding
import com.dragonguard.android.model.rankings.TotalUsersRankingsModel

/*
 모든 사용자들을 랭킹 순서대로 나열하기 위한 recycleradapter
 */
class TotalUsersRankingAdapter (private val datas : ArrayList<TotalUsersRankingsModel>,
                                private val context: Context, private val token: String) : RecyclerView.Adapter<TotalUsersRankingAdapter.ViewHolder>() {
    private lateinit var binding: TotalUsersRankingListBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = TotalUsersRankingListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root)
    }
    override fun getItemCount(): Int = datas.size

    //리사이클러 뷰의 요소들을 넣어줌
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(data1: TotalUsersRankingsModel) {
            if(data1.github_id != null && data1.ranking != null && data1.tokens !=null) {
                binding.totalUsersRanking.text = data1.ranking.toString()
                binding.rankerId.text = data1.github_id
//                Toast.makeText(context, "${data.tokens}", Toast.LENGTH_SHORT).show()
                if(data1.tokens == null) {
                    binding.rankerContribution.text = "NONE"
                } else {
                    binding.rankerContribution.text = data1.tokens.toString()
                }
                binding.rankerId.setOnClickListener {
                    Intent(context, RepoContributorsActivity::class.java).apply{
                        putExtra("githubId", data1.github_id)
                        putExtra("token", token)
                    }.run{context.startActivity(this)}
                }
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
        return position
    }

}