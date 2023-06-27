package com.dragonguard.android.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dragonguard.android.databinding.RepoCompareListBinding
import com.dragonguard.android.model.compare.RepoStats
import kotlin.math.round

class RepoCompareAdapter(private val data1 : RepoStats, private val data2 : RepoStats, private val compareItems: ArrayList<String>) : RecyclerView.Adapter<RepoCompareAdapter.ViewHolder>() {
    private lateinit var binding: RepoCompareListBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = RepoCompareListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root)
    }
    override fun getItemCount(): Int = compareItems.size

    //리사이클러 뷰의 요소들을 넣어줌
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(data1 : RepoStats, data2 : RepoStats, position: Int) {
            data1.git_repo!!
            data2.git_repo!!
            data1.statistics!!
            data2.statistics!!
            data1.languages_stats!!
            data2.languages_stats!!
            binding.compareHead.text = compareItems[position]
            when(position){
                0->{
                    binding.repo1Value.text = data1.git_repo.forks_count.toString()
                    binding.repo2Value.text = data2.git_repo.forks_count.toString()
                }
                1->{
                    binding.repo1Value.text = data1.git_repo.closed_issues_count.toString()
                    binding.repo2Value.text = data2.git_repo.closed_issues_count.toString()
                }
                2->{
                    binding.repo1Value.text = data1.git_repo.open_issues_count.toString()
                    binding.repo2Value.text = data2.git_repo.open_issues_count.toString()
                }
                3->{
                    binding.repo1Value.text = data1.git_repo.stargazers_count.toString()
                    binding.repo2Value.text = data2.git_repo.stargazers_count.toString()
                }
                4->{
                    binding.repo1Value.text = data1.git_repo.subscribers_count.toString()
                    binding.repo2Value.text = data2.git_repo.subscribers_count.toString()
                }
                5->{
                    binding.repo1Value.text = data1.git_repo.watchers_count.toString()
                    binding.repo2Value.text = data2.git_repo.watchers_count.toString()
                }
                6->{
                    binding.repo1Value.text = data1.statistics.commit_stats.sum.toString()
                    binding.repo2Value.text = data2.statistics.commit_stats.sum.toString()
                }
                7->{
                    binding.repo1Value.text = data1.statistics.commit_stats.max.toString()
                    binding.repo2Value.text = data2.statistics.commit_stats.max.toString()
                }
                8->{
                    binding.repo1Value.text = data1.statistics.commit_stats.min.toString()
                    binding.repo2Value.text = data2.statistics.commit_stats.min.toString()
                }
                9->{
                    binding.repo1Value.text = data1.statistics.commit_stats.count.toString()
                    binding.repo2Value.text = data2.statistics.commit_stats.count.toString()
                }
                10->{
                    binding.repo1Value.text = ((round(data1.statistics.commit_stats.average*100.0))/100.0).toString()
                    binding.repo2Value.text = ((round(data2.statistics.commit_stats.average*100.0))/100.0).toString()
                }
                11->{
                    binding.repo1Value.text = data1.statistics.addition_stats.sum.toString()
                    binding.repo2Value.text = data2.statistics.addition_stats.sum.toString()
                }
                12->{
                    binding.repo1Value.text = data1.statistics.addition_stats.max.toString()
                    binding.repo2Value.text = data2.statistics.addition_stats.max.toString()
                }
                13->{
                    binding.repo1Value.text = data1.statistics.addition_stats.min.toString()
                    binding.repo2Value.text = data2.statistics.addition_stats.min.toString()
                }
                14->{
                    binding.repo1Value.text = data1.statistics.addition_stats.count.toString()
                    binding.repo2Value.text = data2.statistics.addition_stats.count.toString()
                }
                15->{
                    binding.repo1Value.text = ((round(data1.statistics.addition_stats.average*100.0))/100.0).toString()
                    binding.repo2Value.text = ((round(data2.statistics.addition_stats.average*100.0))/100.0).toString()
                }
                16->{
                    binding.repo1Value.text = data1.statistics.deletion_stats.sum.toString()
                    binding.repo2Value.text = data2.statistics.deletion_stats.sum.toString()
                }
                17->{
                    binding.repo1Value.text = data1.statistics.deletion_stats.max.toString()
                    binding.repo2Value.text = data2.statistics.deletion_stats.max.toString()
                }
                18->{
                    binding.repo1Value.text = data1.statistics.deletion_stats.min.toString()
                    binding.repo2Value.text = data2.statistics.deletion_stats.min.toString()
                }
                19->{
                    binding.repo1Value.text = data1.statistics.deletion_stats.count.toString()
                    binding.repo2Value.text = data2.statistics.deletion_stats.count.toString()
                }
                20->{
                    binding.repo1Value.text = ((round(data1.statistics.deletion_stats.average*100.0))/100.0).toString()
                    binding.repo2Value.text = ((round(data2.statistics.deletion_stats.average*100.0))/100.0).toString()
                }
                21->{
                    binding.repo1Value.text = data1.languages_stats.count.toString()
                    binding.repo2Value.text = data2.languages_stats.count.toString()
                }
                22->{
                    binding.repo1Value.text = ((round(data1.languages_stats.average*100.0))/100.0).toString()
                    binding.repo2Value.text = ((round(data2.languages_stats.average*100.0))/100.0).toString()
                }
            }
        }
    }
    override fun getItemViewType(position: Int): Int {
        return position
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data1, data2, position)
    }

}