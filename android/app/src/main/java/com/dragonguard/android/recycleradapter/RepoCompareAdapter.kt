package com.dragonguard.android.recycleradapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dragonguard.android.R
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
            data1.gitRepo!!
            data2.gitRepo!!
            data1.statistics!!
            data2.statistics!!
            data1.languagesStats!!
            data2.languagesStats!!
            binding.compareHead.text = compareItems[position]
            when(position){
                0->{
                    binding.repo1Value.text = data1.gitRepo.forks_count.toString()
                    binding.repo2Value.text = data2.gitRepo.forks_count.toString()
                }
                1->{
                    binding.repo1Value.text = data1.gitRepo.closed_issues_count.toString()
                    binding.repo2Value.text = data2.gitRepo.closed_issues_count.toString()
                }
                2->{
                    binding.repo1Value.text = data1.gitRepo.open_issues_count.toString()
                    binding.repo2Value.text = data2.gitRepo.open_issues_count.toString()
                }
                3->{
                    binding.repo1Value.text = data1.gitRepo.stargazers_count.toString()
                    binding.repo2Value.text = data2.gitRepo.stargazers_count.toString()
                }
                4->{
                    binding.repo1Value.text = data1.gitRepo.subscribers_count.toString()
                    binding.repo2Value.text = data2.gitRepo.subscribers_count.toString()
                }
                5->{
                    binding.repo1Value.text = data1.gitRepo.watchers_count.toString()
                    binding.repo2Value.text = data2.gitRepo.watchers_count.toString()
                }
                6->{
                    binding.repo1Value.text = data1.statistics.commitStats.sum.toString()
                    binding.repo2Value.text = data2.statistics.commitStats.sum.toString()
                }
                7->{
                    binding.repo1Value.text = data1.statistics.commitStats.max.toString()
                    binding.repo2Value.text = data2.statistics.commitStats.max.toString()
                }
                8->{
                    binding.repo1Value.text = data1.statistics.commitStats.min.toString()
                    binding.repo2Value.text = data2.statistics.commitStats.min.toString()
                }
                9->{
                    binding.repo1Value.text = data1.statistics.commitStats.count.toString()
                    binding.repo2Value.text = data2.statistics.commitStats.count.toString()
                }
                10->{
                    binding.repo1Value.text = ((round(data1.statistics.commitStats.average*100.0))/100.0).toString()
                    binding.repo2Value.text = ((round(data2.statistics.commitStats.average*100.0))/100.0).toString()
                }
                11->{
                    binding.repo1Value.text = data1.statistics.additionStats.sum.toString()
                    binding.repo2Value.text = data2.statistics.additionStats.sum.toString()
                }
                12->{
                    binding.repo1Value.text = data1.statistics.additionStats.max.toString()
                    binding.repo2Value.text = data2.statistics.additionStats.max.toString()
                }
                13->{
                    binding.repo1Value.text = data1.statistics.additionStats.min.toString()
                    binding.repo2Value.text = data2.statistics.additionStats.min.toString()
                }
                14->{
                    binding.repo1Value.text = data1.statistics.additionStats.count.toString()
                    binding.repo2Value.text = data2.statistics.additionStats.count.toString()
                }
                15->{
                    binding.repo1Value.text = ((round(data1.statistics.additionStats.average*100.0))/100.0).toString()
                    binding.repo2Value.text = ((round(data2.statistics.additionStats.average*100.0))/100.0).toString()
                }
                16->{
                    binding.repo1Value.text = data1.statistics.deletionStats.sum.toString()
                    binding.repo2Value.text = data2.statistics.deletionStats.sum.toString()
                }
                17->{
                    binding.repo1Value.text = data1.statistics.deletionStats.max.toString()
                    binding.repo2Value.text = data2.statistics.deletionStats.max.toString()
                }
                18->{
                    binding.repo1Value.text = data1.statistics.deletionStats.min.toString()
                    binding.repo2Value.text = data2.statistics.deletionStats.min.toString()
                }
                19->{
                    binding.repo1Value.text = data1.statistics.deletionStats.count.toString()
                    binding.repo2Value.text = data2.statistics.deletionStats.count.toString()
                }
                20->{
                    binding.repo1Value.text = ((round(data1.statistics.deletionStats.average*100.0))/100.0).toString()
                    binding.repo2Value.text = ((round(data2.statistics.deletionStats.average*100.0))/100.0).toString()
                }
                21->{
                    binding.repo1Value.text = data1.languagesStats.count.toString()
                    binding.repo2Value.text = data2.languagesStats.count.toString()
                }
                22->{
                    binding.repo1Value.text = ((round(data1.languagesStats.average*100.0))/100.0).toString()
                    binding.repo2Value.text = ((round(data2.languagesStats.average*100.0))/100.0).toString()
                }
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data1, data2, position)
    }

}