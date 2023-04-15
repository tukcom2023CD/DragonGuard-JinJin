package com.dragonguard.android.activity.search

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.dragonguard.android.R
import com.dragonguard.android.activity.MainActivity
import com.dragonguard.android.databinding.ActivityRepoContributorsBinding
import com.dragonguard.android.model.contributors.RepoContributorsItem
import com.dragonguard.android.recycleradapter.ContributorsAdapter
import com.dragonguard.android.viewmodel.Viewmodel
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import kotlinx.coroutines.*

/*
 선택한 repo의 contributor들과 기여 정도를 보여주고
 막대그래프로 시각화해서 보여주는 activity
 */
class RepoContributorsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRepoContributorsBinding
    lateinit var contributorsAdapter: ContributorsAdapter
    private var contributors = ArrayList<RepoContributorsItem>()
    private var repoName = ""
    var viewmodel = Viewmodel()
    private var count = 0
    private val colorsets = ArrayList<Int>()
    private var token = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_repo_contributors)
        binding.repoContributorsViewmodel = viewmodel

        setSupportActionBar(binding.toolbar) //커스텀한 toolbar를 액션바로 사용
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
        val intent = intent
        token = intent.getStringExtra("token")!!

        repoName = intent.getStringExtra("repoName")!!
//        Toast.makeText(applicationContext, "reponame = $repoName", Toast.LENGTH_SHORT).show()
        repoContributors(repoName)
    }

    //    repo의 contributors 검색
    fun repoContributors(repoName: String) {
        if(!this@RepoContributorsActivity.isFinishing){
            val coroutine = CoroutineScope(Dispatchers.Main)
            coroutine.launch {
                val resultDeferred = coroutine.async(Dispatchers.IO) {
                    viewmodel.getRepoContributors(repoName, token)
                }
                val result = resultDeferred.await()
//            Toast.makeText(applicationContext, "result = ${result.size}",Toast.LENGTH_SHORT).show()
                checkContributors(result)
            }
        }
    }

    //    검색한 결과가 잘 왔는지 확인
    fun checkContributors(result: ArrayList<RepoContributorsItem>) {
        if (result.isNotEmpty()) {
            if (result[0].additions == null) {
                count++
                val handler = Handler(Looper.getMainLooper())
                handler.postDelayed({ repoContributors(repoName) }, 2000)
            } else {
                for (i in 0 until result.size) {
                    val compare = contributors.filter { it.githubId == result[i].githubId }
                    if (compare.isEmpty()) {
                        contributors.add(result[i])
                    }
                }
                initRecycler()
            }
        } else {
            if (count<10) {
                count++
                val handler = Handler(Looper.getMainLooper())
                handler.postDelayed({ repoContributors(repoName) }, 2000)
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    //    리사이클러뷰 실행
    private fun initRecycler() {
        binding.repoContributors.setItemViewCacheSize(contributors.size)
//        Toast.makeText(applicationContext, "리사이클러뷰 시작", Toast.LENGTH_SHORT).show()
//        Toast.makeText(applicationContext, "contributors 수 : ${contributors.size}", Toast.LENGTH_SHORT).show()
        contributorsAdapter = ContributorsAdapter(contributors, this, colorsets, token, repoName)
        binding.repoContributors.adapter = contributorsAdapter
        binding.repoContributors.layoutManager = LinearLayoutManager(this)
        binding.repoContributors.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
        initGraph()
    }

    //    그래프 그리기
    private fun initGraph() {
//        Toast.makeText(applicationContext,"그래프 그리기 시작", Toast.LENGTH_SHORT).show()
        val entries = ArrayList<BarEntry>()
        var count = 1
        contributors.forEach {
            entries.add(BarEntry(count.toFloat(), it.commits!!.toFloat()))
            count++
//                Toast.makeText(applicationContext, "현재 count = $count", Toast.LENGTH_SHORT).show()
        }

        val set = BarDataSet(entries,"DataSet")
        set.colors = colorsets
        set.apply{
            formSize = 15f
            valueTextSize = 12f
            setDrawValues(true)
            valueFormatter = ScoreCustomFormatter(contributors)
            setDrawIcons(true)
        }
        val dataSet = ArrayList<IBarDataSet>()
        dataSet.add(set)
        val data = BarData()
        data.addDataSet(set)

        binding.contributorsChart.apply {
//            Toast.makeText(applicationContext, "그래프 entry  = ${entries.size}", Toast.LENGTH_SHORT).show()
//            setDrawValueAboveBar(true) // 입력?값이 차트 위or아래에 그려질 건지 (true=위, false=아래)
//            setMaxVisibleValueCount(entries.size) // 최대 보이는 그래프 개수를 contributors의 개수로 지정
            setDrawBarShadow(false) // 그래프 그림자
            setTouchEnabled(false) // 차트 터치 막기
            setPinchZoom(false) // 두손가락으로 줌 설정
            setDrawGridBackground(false) // 격자구조
            description.isEnabled = false // 그래프 오른쪽 하단에 라벨 표시
            legend.isEnabled = false // 차트 범례 설정(legend object chart)
            axisRight.isEnabled = false // 오른쪽 Y축을 안보이게 해줌.
            axisLeft.apply { //왼쪽 축. 즉 Y방향 축을 뜻한다.
                axisMinimum = 0f // 최소값 0
                granularity = 10f // 10 단위마다 선을 그리려고 설정.
                setDrawLabels(true) // 값 적는거 허용 (0, 50, 100)
                setDrawGridLines(true) //격자 라인 활용
                setDrawAxisLine(true) // 축 그리기 설정
                axisLineColor = ContextCompat.getColor(context, R.color.black) // 축 색깔 설정
                gridColor = ContextCompat.getColor(context, R.color.black) // 축 아닌 격자 색깔 설정
                textColor = ContextCompat.getColor(context, R.color.black) // 라벨 텍스트 컬러 설정
                textSize = 13f //라벨 텍스트 크기
            }
            xAxis.apply {
                yOffset = 0f
                isEnabled = true
                position = XAxis.XAxisPosition.BOTTOM //X축을 아래에다가 둔다.
                granularity = 1f // 1 단위만큼 간격 두기
                setDrawAxisLine(true) // 축 그림
                setDrawGridLines(false) // 격자
                textColor = ContextCompat.getColor(context, R.color.black) //라벨 색상
                textSize = 12f // 텍스트 크기
                valueFormatter = MyXAxisFormatter(contributors) // X축 라벨값(밑에 표시되는 글자) 바꿔주기 위해 설정
            }
            animateY(500) // 밑에서부터 올라오는 애니매이션 적용
        }

        data.apply {
            setValueTextSize(12f)
            barWidth = 0.3f //막대 너비 설정
        }
        binding.contributorsChart.data = data
        binding.contributorsChart.invalidate()
        binding.contributorsChart.visibility = View.VISIBLE
//        binding.contributorsChart.run {
//            this.data = data //차트의 데이터를 data로 설정해줌.
//            setFitBars(true)
//
//            invalidate()
//        }
        count = 0
    }

    /*    그래프 x축을 contributor의 이름으로 변경하는 코드
          x축 label을 githubId의 앞의 4글자를 기입하여 곂치는 문제 해결
     */
    class MyXAxisFormatter(contributors: ArrayList<RepoContributorsItem>) : ValueFormatter() {
        private val days = contributors.flatMap { arrayListOf(it.githubId!!.substring(0,4)) }
        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            return days.getOrNull(value.toInt() - 1) ?: value.toString()
        }
        override fun getFormattedValue(value: Float): String {
            return "" + value.toInt()
        }
    }

//    막대 위의 커밋수 정수로 변경
    class ScoreCustomFormatter(contributors: ArrayList<RepoContributorsItem>) : ValueFormatter() {
        private val days = contributors.flatMap { arrayListOf(it.githubId!!.substring(0,4)) }
        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            return days.getOrNull(value.toInt() - 1) ?: value.toString().substring(0,2)
        }
        override fun getFormattedValue(value: Float): String {
            return "" + value.toInt()
        }
    }

//    뒤로가기 누르면 화면 전환하게 함
    override fun onBackPressed() {
        val intent = Intent(applicationContext, SearchActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home, binding.toolbar.menu)
        return true
    }

//    뒤로가기, 홈으로 화면전환 기능
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