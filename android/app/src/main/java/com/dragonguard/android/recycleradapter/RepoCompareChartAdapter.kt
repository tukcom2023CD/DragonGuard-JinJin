package com.dragonguard.android.recycleradapter

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
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
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IValueFormatter
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet
import kotlin.math.absoluteValue


class RepoCompareChartAdapter(private val data1: FirstRepo, private val data2: SecondRepo, private val context: Context) :
    RecyclerView.Adapter<RepoCompareChartAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.repo_compare_chart, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = 3

    //리사이클러 뷰의 요소들을 넣어줌
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val radar: RadarChart = itemView.findViewById(R.id.repo_compare_chart)
        private val pie: PieChart = itemView.findViewById(R.id.repo_compare_language)
        fun bind(data1: FirstRepo, data2: SecondRepo, position1: Int, context: Context) {
            data1.languages!!
            data1.gitRepo!!
            data1.statistics!!
            data1.languagesStats!!
            data2.languages!!
            data2.gitRepo!!
            data2.statistics!!
            data2.languagesStats!!
            var count = 0
            val sum = data1.languages.values.sum()
            val colors = ArrayList<Int>()
            var red = 0
            var green = 0
            var blue = 0
            if (position1 % 3 == 1) {
                val entries = ArrayList<PieEntry>()
                data1.languages.forEach {
                    entries.add(PieEntry(it.value.toFloat() / sum, it.key))
                    red = (Math.random() * 255).toInt()
                    green = (Math.random() * 255).toInt()
                    blue = (Math.random() * 255).toInt()
                    colors.add(Color.rgb(red, green, blue))
                    count++
                }
                val dataSet = PieDataSet(entries, data1.gitRepo.full_name)
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

            } else if (position1 % 3 == 2) {
                val entries = ArrayList<PieEntry>()
                data2.languages!!.forEach {
                    entries.add(PieEntry(it.value.toFloat() / sum, it.key))
                    red = (Math.random() * 255).toInt()
                    green = (Math.random() * 255).toInt()
                    blue = (Math.random() * 255).toInt()
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
                pie.setEntryLabelColor(Color.BLACK)
                pie.invalidate()
                radar.visibility = View.GONE
                pie.visibility = View.VISIBLE

            } else {
                val entries1 = ArrayList<RadarEntry>()
                entries1.add(RadarEntry(data1.statistics.additionStats.average.toFloat()))
                entries1.add(RadarEntry(data1.statistics.deletionStats.average.toFloat()))
//                entries1.add(RadarEntry(data1.statistics.additionStats.max.toFloat()))
                entries1.add(RadarEntry(data1.languagesStats.min.toFloat()))
//                Toast.makeText(context, "${data1.statistics.additionStats.average.toFloat()}, ${data1.statistics.deletionStats.average.toFloat()}, ${data1.languagesStats.min}",Toast.LENGTH_SHORT).show()
                val entries2 = ArrayList<RadarEntry>()
                entries2.add(RadarEntry(data2.statistics.additionStats.average.toFloat()))
                entries2.add(RadarEntry(data2.statistics.deletionStats.average.toFloat()))
//                entries2.add(RadarEntry(data2.statistics.additionStats.max.toFloat()))
                entries2.add(RadarEntry(data2.languagesStats.average.toFloat()))
                val name1 = data1.gitRepo.full_name.split("/","_","-")
                val name2 = data2.gitRepo.full_name.split("/","_","-")
                var short1 = ""
                var short2 = ""
                if(name1.size > 2) {
                    short1 = "${name1[name1.lastIndex-1]}\n${name1.last()}"
                } else {
                    short1 = name1.last()
                }
                if(name2.size > 2) {
                    short2 = "${name2[name2.lastIndex-1]}\n${name2.last()}"
                } else {
                    short2 = name2.last()
                }
                val set1 = RadarDataSet(entries1, short1)
//                set1.label = null
                set1.setDrawFilled(true)
                set1.color = Color.GREEN
                set1.fillColor = Color.GREEN
                set1.fillAlpha = 80
                set1.setDrawValues(false)
                val set2 = RadarDataSet(entries2, short2)
//                set2.label = null
                set2.setDrawFilled(true)
                set2.color = Color.BLACK
                set2.fillColor = Color.BLACK
                set2.fillAlpha = 80
                set2.setDrawValues(false)
//                val dataSets1: ArrayList<IRadarDataSet> = ArrayList()
//                dataSets1.add(set1)
                val data1 = RadarData(set1)
                data1.addDataSet(set2)
                radar.apply {
                    setTouchEnabled(false) // 차트 터치 막기
                    description.isEnabled = false // 그래프 오른쪽 하단에 라벨 표시
                    legend.isEnabled = true // 차트 범례 설정(legend object chart)
                    xAxis.apply {
                        isEnabled = true
                        position = XAxis.XAxisPosition.BOTTOM //X축을 아래에다가 둔다.
                        granularity = 1f // 1 단위만큼 간격 두기
                        setDrawAxisLine(true) // 축 그림
                        setDrawGridLines(false) // 격자
                        textColor = ContextCompat.getColor(context, R.color.black) //라벨 색상
                        valueFormatter = MyXAxisFormatter()
                        textSize = 12f // 텍스트 크기
                    }
                }
                radar.rotation = 0f
                radar.data = data1
                radar.invalidate()
                pie.visibility = View.GONE
                radar.visibility = View.VISIBLE
            }
        }
    }

    class MyXAxisFormatter() : ValueFormatter() {
        private val days = listOf("addition average", "deletion average", "language average")
        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            return days.getOrNull(value.toInt()) ?: value.toString()
        }
        override fun getFormattedValue(value: Float): String {
            return "" + value.toInt()
        }
    }

    class ScoreCustomFormatter() : ValueFormatter() {
        private val days = listOf("addition average", "deletion average", "language average")

        //        private val days = listOf( "additions", "deletions")
        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            return days.getOrNull(value.toInt()) ?: value.toString()
        }

        override fun getFormattedValue(value: Float): String {
            return "" + value.toInt()
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data1, data2, position, context)
    }

}