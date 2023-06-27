package com.dragonguard.android.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.dragonguard.android.R
import com.dragonguard.android.databinding.RepoCompareChartBinding
import com.dragonguard.android.model.compare.RepoStats
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet


//viewpager의 슬라이딩 구현을 위한 adapter
class RepoCompareChartAdapter(private val data1: RepoStats, private val data2: RepoStats, private val context: Context) :
    RecyclerView.Adapter<RepoCompareChartAdapter.ViewHolder>() {
    private lateinit var binding: RepoCompareChartBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = RepoCompareChartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root)
    }

    override fun getItemCount(): Int = 3

    //리사이클러 뷰의 요소들을 넣어줌
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(data1: RepoStats, data2: RepoStats, position1: Int, context: Context) {
            data1.languages!!
            data1.git_repo!!
            data1.statistics!!
            data1.languages_stats!!
            data2.languages!!
            data2.git_repo!!
            data2.statistics!!
            data2.languages_stats!!
            var count = 0
            val sum = data1.languages.values.sum()
            val colors = ArrayList<Int>()
            var red = 0
            var green = 0
            var blue = 0

            /*
            position ==1 -> 첫번째 Repository의 언어들의 비중을 보여주는 pie chart 그리기
            position ==2 -> 두번째 Repository의 언어들의 비중을 보여주는 pie chart 그리기
            position ==0 -> 두 Repository의 addition average, deletion average, language average를 비교하는 radar chart 그리기
             */
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
                val dataSet = PieDataSet(entries, data1.git_repo.full_name)
                dataSet.label = null
                dataSet.setDrawValues(false)
                dataSet.colors = colors
                val data = PieData(dataSet)
                binding.repoCompareLanguage.setTouchEnabled(false)
                binding.repoCompareLanguage.description.isEnabled = false
                binding.repoCompareLanguage.data = data
                binding.repoCompareLanguage.setDrawEntryLabels(false)
//                pie.setEntryLabelColor(Color.BLACK)
                binding.repoCompareLanguage.invalidate()
                binding.repoCompareChart.visibility = View.GONE
                binding.repoCompareLanguage.visibility = View.VISIBLE

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
                val dataSet = PieDataSet(entries, data2.git_repo!!.full_name)
                dataSet.label = null
                dataSet.colors = colors
                dataSet.setDrawValues(false)
                val data = PieData(dataSet)
                binding.repoCompareLanguage.setTouchEnabled(false)
                binding.repoCompareLanguage.description.isEnabled = false
                binding.repoCompareLanguage.data = data
                binding.repoCompareLanguage.setDrawEntryLabels(false)
                binding.repoCompareLanguage.setEntryLabelColor(Color.BLACK)
                binding.repoCompareLanguage.invalidate()
                binding.repoCompareChart.visibility = View.GONE
                binding.repoCompareLanguage.visibility = View.VISIBLE

            } else {
                val entries1 = ArrayList<RadarEntry>()
                entries1.add(RadarEntry(data1.statistics.addition_stats.average.toFloat()))
                entries1.add(RadarEntry(data1.statistics.deletion_stats.average.toFloat()))
//                entries1.add(RadarEntry(data1.statistics.additionStats.max.toFloat()))
                entries1.add(RadarEntry(data1.languages_stats.average.toFloat()))
//                Toast.makeText(context, "${data1.statistics.additionStats.average.toFloat()}, ${data1.statistics.deletionStats.average.toFloat()}, ${data1.languagesStats.min}",Toast.LENGTH_SHORT).show()
                val entries2 = ArrayList<RadarEntry>()
                entries2.add(RadarEntry(data2.statistics.addition_stats.average.toFloat()))
                entries2.add(RadarEntry(data2.statistics.deletion_stats.average.toFloat()))
//                entries2.add(RadarEntry(data2.statistics.additionStats.max.toFloat()))
                entries2.add(RadarEntry(data2.languages_stats.average.toFloat()))
                val name1 = data1.git_repo.full_name.split("/","_","-")
                val name2 = data2.git_repo.full_name.split("/","_","-")
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
                binding.repoCompareChart.setExtraOffsets(0f, 0f, 0f, 0f)
                binding.repoCompareChart.animateY(500)

                binding.repoCompareChart.xAxis.setAvoidFirstLastClipping(true)
                binding.repoCompareChart.scaleX = 1.3f
                binding.repoCompareChart.scaleY = 1.3f
                binding.repoCompareChart.apply {
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
                binding.repoCompareChart.rotation = 0f
                binding.repoCompareChart.data = data1
                binding.repoCompareChart.invalidate()
                binding.repoCompareLanguage.visibility = View.GONE
                binding.repoCompareChart.visibility = View.VISIBLE
            }
        }
    }
    override fun getItemViewType(position: Int): Int {
        return position
    }

    //x축 label을 수정하기 위한 valueformatter
    class MyXAxisFormatter() : ValueFormatter() {
        private val days = listOf("addition average", "deletion average", "language average")
        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            return days.getOrNull(value.toInt()) ?: value.toString()
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