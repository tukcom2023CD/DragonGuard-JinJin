package com.dragonguard.android.recycleradapter

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import androidx.recyclerview.widget.RecyclerView
import com.dragonguard.android.R
import com.dragonguard.android.model.FirstRepo
import com.dragonguard.android.model.FirstResult
import com.dragonguard.android.model.SecondRepo
import com.dragonguard.android.model.SecondResult
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.charts.RadarChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import kotlin.math.absoluteValue


class RepoCompareChartAdapter (private val data1 : FirstRepo, private val data2 : SecondRepo) : RecyclerView.Adapter<RepoCompareChartAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.repo_compare_chart,parent,false)
        return ViewHolder(view)
    }
    override fun getItemCount(): Int = 3

    //리사이클러 뷰의 요소들을 넣어줌
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val radar: RadarChart = itemView.findViewById(R.id.repo_compare_chart)
        private val pie: PieChart = itemView.findViewById(R.id.repo_compare_language)
        fun bind(data1 : FirstRepo, data2 : SecondRepo, position: Int) {
            var count = 0
            val sum = data1.languages!!.values.sum()
            val colors = ArrayList<Int>()
            var red = 0
            var green = 0
            var blue = 0
            if(position%3 == 1){
                val entries = ArrayList<PieEntry>()
                data1.languages.forEach {
                    entries.add(PieEntry(it.value.toFloat()/sum, it.key))
                    red = (Math.random()*255).toInt()
                    green = (Math.random()*255).toInt()
                    blue = (Math.random()*255).toInt()
                    colors.add(Color.rgb(red, green, blue))
                    count++
                }
                val dataSet = PieDataSet(entries, data1.gitRepo!!.full_name)
                dataSet.label = null
                dataSet.setDrawValues(false)
                dataSet.colors = colors
                val data = PieData(dataSet)
                pie.setTouchEnabled(false)
                pie.description.isEnabled = false
                pie.data = data
                pie.setDrawEntryLabels(false)
//                pie.setEntryLabelColor(Color.BLACK)
                pie.invalidate()
                radar.visibility = View.GONE
                pie.visibility = View.VISIBLE

            } else if(position%3 == 2){
                val entries = ArrayList<PieEntry>()
                data2.languages!!.forEach {
                    entries.add(PieEntry(it.value.toFloat()/sum, it.key))
                    red = (Math.random()*255).toInt()
                    green = (Math.random()*255).toInt()
                    blue = (Math.random()*255).toInt()
                    colors.add(Color.rgb(red, green, blue))
                    count++
                }
                val dataSet = PieDataSet(entries, data2.gitRepo!!.full_name)
                dataSet.label = null
                dataSet.colors = colors
                dataSet.setDrawValues(false)
                val data = PieData(dataSet)
                pie.setTouchEnabled(false)
                pie.description.isEnabled = false
                pie.data = data
                pie.setDrawEntryLabels(false)
//                pie.setEntryLabelColor(Color.BLACK)
                pie.invalidate()
                radar.visibility = View.GONE
                pie.visibility = View.VISIBLE

            } else {

            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data1, data2, position)
    }

}