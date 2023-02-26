package com.dragonguard.android.recycleradapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dragonguard.android.R
import com.dragonguard.android.model.FirstRepo
import com.dragonguard.android.model.SecondRepo
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.charts.RadarChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import kotlin.math.round

class RepoCompareAdapter(private val data1 : FirstRepo, private val data2 : SecondRepo, private val compareItems: ArrayList<String>) : RecyclerView.Adapter<RepoCompareAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.repo_compare_list,parent,false)
        return ViewHolder(view)
    }
    override fun getItemCount(): Int = compareItems.size

    //리사이클러 뷰의 요소들을 넣어줌
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val compareHead = view.findViewById<TextView>(R.id.compare_head)
        private val repo1Value = view.findViewById<TextView>(R.id.repo1_value)
        private val repo2Value = view.findViewById<TextView>(R.id.repo2_value)

        fun bind(data1 : FirstRepo, data2 : SecondRepo, position: Int) {
            data1.gitRepo!!
            data2.gitRepo!!
            data1.statistics!!
            data2.statistics!!
            data1.languagesStats!!
            data2.languagesStats!!
            compareHead.text = compareItems[position]
            when(position){
                0->{
                    repo1Value.text = data1.gitRepo.forks_count.toString()
                    repo2Value.text = data2.gitRepo.forks_count.toString()
                }
                1->{
                    repo1Value.text = data1.gitRepo.closed_issues_count.toString()
                    repo2Value.text = data2.gitRepo.closed_issues_count.toString()
                }
                2->{
                    repo1Value.text = data1.gitRepo.open_issues_count.toString()
                    repo2Value.text = data2.gitRepo.open_issues_count.toString()
                }
                3->{
                    repo1Value.text = data1.gitRepo.stargazers_count.toString()
                    repo2Value.text = data2.gitRepo.stargazers_count.toString()
                }
                4->{
                    repo1Value.text = data1.gitRepo.subscribers_count.toString()
                    repo2Value.text = data2.gitRepo.subscribers_count.toString()
                }
                5->{
                    repo1Value.text = data1.gitRepo.watchers_count.toString()
                    repo2Value.text = data2.gitRepo.watchers_count.toString()
                }
                6->{
                    repo1Value.text = data1.statistics.commitStats.sum.toString()
                    repo2Value.text = data2.statistics.commitStats.sum.toString()
                }
                7->{
                    repo1Value.text = data1.statistics.commitStats.max.toString()
                    repo2Value.text = data2.statistics.commitStats.max.toString()
                }
                8->{
                    repo1Value.text = data1.statistics.commitStats.min.toString()
                    repo2Value.text = data2.statistics.commitStats.min.toString()
                }
                9->{
                    repo1Value.text = data1.statistics.commitStats.count.toString()
                    repo2Value.text = data2.statistics.commitStats.count.toString()
                }
                10->{
                    repo1Value.text = (round(data1.statistics.commitStats.average)/100).toString()
                    repo2Value.text = (round(data2.statistics.commitStats.average)/100).toString()
                }
                11->{
                    repo1Value.text = data1.statistics.additionStats.sum.toString()
                    repo2Value.text = data2.statistics.additionStats.sum.toString()
                }
                12->{
                    repo1Value.text = data1.statistics.additionStats.max.toString()
                    repo2Value.text = data2.statistics.additionStats.max.toString()
                }
                13->{
                    repo1Value.text = data1.statistics.additionStats.min.toString()
                    repo2Value.text = data2.statistics.additionStats.min.toString()
                }
                14->{
                    repo1Value.text = data1.statistics.additionStats.count.toString()
                    repo2Value.text = data2.statistics.additionStats.count.toString()
                }
                15->{
                    repo1Value.text = (round(data1.statistics.additionStats.average)/100).toString()
                    repo2Value.text = (round(data2.statistics.additionStats.average)/100).toString()
                }
                16->{
                    repo1Value.text = data1.statistics.deletionStats.sum.toString()
                    repo2Value.text = data2.statistics.deletionStats.sum.toString()
                }
                17->{
                    repo1Value.text = data1.statistics.deletionStats.max.toString()
                    repo2Value.text = data2.statistics.deletionStats.max.toString()
                }
                18->{
                    repo1Value.text = data1.statistics.deletionStats.min.toString()
                    repo2Value.text = data2.statistics.deletionStats.min.toString()
                }
                19->{
                    repo1Value.text = data1.statistics.deletionStats.count.toString()
                    repo2Value.text = data2.statistics.deletionStats.count.toString()
                }
                20->{
                    repo1Value.text = (round(data1.statistics.deletionStats.average)/100).toString()
                    repo2Value.text = (round(data2.statistics.deletionStats.average)/100).toString()
                }
                21->{
                    repo1Value.text = data1.languagesStats.count.toString()
                    repo2Value.text = data2.languagesStats.count.toString()
                }
                22->{
                    repo1Value.text = (round(data1.languagesStats.average)/100).toString()
                    repo2Value.text = (round(data2.languagesStats.average)/100).toString()
                }
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data1, data2, position)
    }

}