package com.dragonguard.android.recycleradapter

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import androidx.recyclerview.widget.RecyclerView
import com.dragonguard.android.R
import com.dragonguard.android.model.*
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


class RepoCompareChartAdapter(private val data1: RepoStats, private val data2: RepoStats, private val context: Context) :
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
        fun bind(data1: RepoStats, data2: RepoStats, position1: Int, context: Context) {
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
                entries1.add(RadarEntry(data1.languagesStats.average.toFloat()))
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
                set1.valueFormatter = ScoreCustomFormatter()
                set1.setDrawFilled(true)
                set1.color = Color.GREEN
                set1.fillColor = Color.GREEN
                set1.fillAlpha = 50
                set1.lineWidth = 2f
                set1.isDrawHighlightCircleEnabled = true
                set1.setDrawValues(false)
                val set2 = RadarDataSet(entries2, short2)
//                set2.label = null
                set2.valueFormatter = ScoreCustomFormatter()
                set2.setDrawFilled(true)
                set2.color = Color.BLACK
                set2.fillColor = Color.BLACK
                set2.fillAlpha = 50
                set2.lineWidth = 2f
                set2.setDrawValues(false)
                set2.isDrawHighlightCircleEnabled = true
                val dataSets1: ArrayList<IRadarDataSet> = ArrayList()
                dataSets1.add(set1)
                val data1 = RadarData(dataSets1)
                data1.addDataSet(set2)
                radar.setExtraOffsets(0f, 0f, 0f, 0f)
                radar.animateY(500)

                radar.xAxis.setAvoidFirstLastClipping(true)
                radar.scaleX = 1.3f
                radar.scaleY = 1.3f
                radar.apply {
                    setTouchEnabled(false) // 차트 터치 막기
                    description.isEnabled = false // 그래프 오른쪽 하단에 라벨 표시
                    legend.isEnabled = false // 차트 범례 설정(legend object chart)
                    //legend.yOffset = 0f
                    //legend.xOffset = 0f
                    yAxis.apply {
                        isEnabled = true
                        axisMinimum = 0f
                        yOffset = 0f
                        textSize = 0f
                        setDrawLabels(false)
                    }
                    xAxis.apply {
                        axisMinimum = 0f
                        yOffset = 0f
                        isEnabled = true
                        position = XAxis.XAxisPosition.BOTTOM //X축을 아래에다가 둔다.
                        setDrawAxisLine(true) // 축 그림
                        setDrawGridLines(false) // 격자
                        textColor = ContextCompat.getColor(context, R.color.black) //라벨 색상
                        valueFormatter = MyXAxisFormatter()
                        textSize = 10f // 텍스트 크기
                        setDrawLabels(true)
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
        private val days = listOf("addition average", "deletion average", "language minimum")
        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            return days.getOrNull(value.toInt()) ?: value.toString()
        }
    }

    class ScoreCustomFormatter() : ValueFormatter() {
        private val days = listOf("addition average", "deletion average", "language minimum")

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