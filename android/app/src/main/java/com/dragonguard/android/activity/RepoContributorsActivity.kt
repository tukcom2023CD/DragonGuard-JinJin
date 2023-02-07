package com.dragonguard.android.activity

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.dragonguard.android.R
import com.dragonguard.android.databinding.ActivityRepoContributorsBinding
import com.dragonguard.android.model.RepoContributorsItem
import com.dragonguard.android.recycleradapter.ContributorsAdapter
import com.dragonguard.android.viewmodel.RepoContributorsViewModel
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import kotlinx.coroutines.*

class RepoContributorsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRepoContributorsBinding
    lateinit var contributorsAdapter: ContributorsAdapter
    private var contributors = ArrayList<RepoContributorsItem>()
    var viewmodel = RepoContributorsViewModel()
    private val colors = ArrayList<Int>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_repo_contributors)
        binding.repoContributorsViewmodel = viewmodel

        setSupportActionBar(binding.toolbar) //커스텀한 toolbar를 액션바로 사용
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)

        val repoName = intent.getStringExtra("repoName")
//        Toast.makeText(applicationContext, "reponame = $repoName", Toast.LENGTH_SHORT).show()
        repoContributors(repoName!!)
    }

    fun repoContributors(repoName: String) {
        val coroutine = CoroutineScope(Dispatchers.Main)
        coroutine.launch {
            var resultDeferred = coroutine.async(Dispatchers.IO) {
                viewmodel.getRepoContributors(repoName)
            }
            var result = resultDeferred.await()
            delay(500)
            if (!checkContributors(result)) {
//                Toast.makeText(applicationContext, "비어있음", Toast.LENGTH_SHORT).show()
                resultDeferred = coroutine.async(Dispatchers.IO) {
                    viewmodel.getRepoContributors(repoName)
                }
                result = resultDeferred.await()
                delay(500)
                checkContributors(result)
            }
        }
    }

    fun checkContributors(result: ArrayList<RepoContributorsItem>): Boolean {
        if (!result.isNullOrEmpty()) {
            for (i in 0 until result.size) {
                val compare = contributors.filter{it.githubId == result[i].githubId}
                if(compare.isNullOrEmpty()) {
                    contributors.add(result[i])
                }
            }
            initRecycler()
            return true
        }
        return false
    }

    private fun initRecycler() {
//        Toast.makeText(applicationContext, "리사이클러뷰 시작", Toast.LENGTH_SHORT).show()
        contributorsAdapter = ContributorsAdapter(contributors, this, colors)
        binding.repoContributors.adapter = contributorsAdapter
        binding.repoContributors.layoutManager = LinearLayoutManager(this)
        binding.repoContributors.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE

        initGraph()
    }

    private fun initGraph() {
//        Toast.makeText(applicationContext,"그래프 그리기 시작", Toast.LENGTH_SHORT).show()
        val entries = ArrayList<BarEntry>()
        binding.contributorsChart.run {
            var count = 1
            contributors.forEach {
                entries.add(BarEntry(count + 0.05f, it.commits.toFloat(), 0f))
                count++
//                Toast.makeText(applicationContext, "현재 count = $count", Toast.LENGTH_SHORT).show()
            }
//            Toast.makeText(applicationContext, "그래프 entry  = ${entries.size}", Toast.LENGTH_SHORT).show()
            setMaxVisibleValueCount(entries.size) // 최대 보이는 그래프 개수를 contributors의 개수로 지정
            setPinchZoom(false) // 핀치줌(두손가락으로 줌인 줌 아웃하는것) 설정
            setDrawBarShadow(false) //그래프의 그림자
            setDrawGridBackground(false)
            description.isEnabled = false
            axisLeft.run { //왼쪽 축. 즉 Y방향 축을 뜻한다.
                axisMinimum = 0f // 최소값 0
                granularity = 50f // 50 단위마다 선을 그리려고 설정.
                setDrawLabels(true) // 값 적는거 허용 (0, 50, 100)
                setDrawGridLines(true) //격자 라인 활용
                setDrawAxisLine(true) // 축 그리기 설정
                axisLineColor = ContextCompat.getColor(context, R.color.black) // 축 색깔 설정
                gridColor = ContextCompat.getColor(context, R.color.purple_200) // 축 아닌 격자 색깔 설정
                textColor = ContextCompat.getColor(context, R.color.black) // 라벨 텍스트 컬러 설정
                textSize = 13f //라벨 텍스트 크기
            }
            xAxis.run {
                position = XAxis.XAxisPosition.BOTTOM //X축을 아래에다가 둔다.
                granularity = 1f // 1 단위만큼 간격 두기
                setDrawAxisLine(true) // 축 그림
                setDrawGridLines(false) // 격자
                textColor = ContextCompat.getColor(context, R.color.black) //라벨 색상
                textSize = 12f // 텍스트 크기
                valueFormatter = MyXAxisFormatter(contributors) // X축 라벨값(밑에 표시되는 글자) 바꿔주기 위해 설정
            }
            axisRight.isEnabled = false // 오른쪽 Y축을 안보이게 해줌.
            setTouchEnabled(false) // 그래프 터치해도 아무 변화없게 막음
//            animateY(1000) // 밑에서부터 올라오는 애니매이션 적용
            legend.isEnabled = false //차트 범례 설정
        }

        var set = BarDataSet(entries,"DataSet") // 데이터셋 초기화
        set.setDrawValues(true)
        set.valueTextSize = 12f
        set.valueTextColor = Color.BLACK
        set.colors = colors
        val dataSet :ArrayList<IBarDataSet> = ArrayList()
        dataSet.add(set)
        val data = BarData(dataSet)
        data.barWidth = 0.3f //막대 너비 설정
        data.setDrawValues(true)
        data.setValueTextSize(12f)
        data.setValueTextColor(Color.BLACK)
        binding.contributorsChart.run {
            this.data = data //차트의 데이터를 data로 설정해줌.
            setFitBars(true)
            invalidate()
        }
    }

    class MyXAxisFormatter(contributors: ArrayList<RepoContributorsItem>) : ValueFormatter() {
        private val days = contributors.flatMap { arrayListOf(it.githubId) }
        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            return days.getOrNull(value.toInt() - 1) ?: value.toString()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home, binding.toolbar.menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
            R.id.home_menu -> {
                val intent = Intent(applicationContext, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}