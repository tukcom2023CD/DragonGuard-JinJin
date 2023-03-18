package com.dragonguard.android.fragment

import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import com.dragonguard.android.R
import com.dragonguard.android.databinding.FragmentCompareUserBinding
import com.dragonguard.android.model.CompareRepoMembersResponseModel
import com.dragonguard.android.model.RepoContributorsItem
import com.dragonguard.android.viewmodel.Viewmodel
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

//선택한 두 Repository의 member들을 비교하기 위한 fragment
class CompareUserFragment(repoName1: String, repoName2: String, token: String) : Fragment() {

    private var repo1 = repoName1
    private var repo2 = repoName2
    private var contributors1 = ArrayList<RepoContributorsItem>()
    private var contributors2 = ArrayList<RepoContributorsItem>()
    private var count = 0
    private lateinit var binding : FragmentCompareUserBinding
    private var viewmodel = Viewmodel()
    private val token = token

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_compare_user, container, false)
        binding.compareUserViewmodel = viewmodel

        return binding.root
    }

    /*
    두 Repository의 멤버를 비교하는 API를 호출하는 함수
    호출 후 이상유무를 확인하는 함수 호출
     */
    fun repoContributors(repoName1: String, repoName2: String) {
        val coroutine = CoroutineScope(Dispatchers.Main)
        coroutine.launch {
            val resultDeferred = coroutine.async(Dispatchers.IO) {
                viewmodel.postCompareRepoMembersRequest(repoName1, repoName2, token)
            }
            val result = resultDeferred.await()
//            Toast.makeText(applicationContext, "result = ${result.size}",Toast.LENGTH_SHORT).show()
            checkContributors(result)
        }
    }

    /*
    검색한 결과가 잘 왔는지 확인하는 함수
    이상없으면 spinner에 두 Repository의 github id 리스트 넣는 함수 호출
     */
    fun checkContributors(result: CompareRepoMembersResponseModel) {
        if ((result.firstResult != null) && (result.secondResult != null)) {
            if (result.firstResult.isEmpty()) {
                count++
                val handler = Handler(Looper.getMainLooper())
                handler.postDelayed({repoContributors(repo1, repo2)}, 5000)
            } else {
                var compare1 = mutableListOf<RepoContributorsItem>()
                for (i in 0 until result.firstResult.size) {
                    compare1 = contributors1.filter { it.githubId == result.firstResult[i].githubId }.toMutableList()
                    if (compare1.isEmpty()) {
                        contributors1.add(RepoContributorsItem(result.firstResult[i].additions,result.firstResult[i].commits,
                            result.firstResult[i].deletions, result.firstResult[i].githubId))
                    } else {
                        compare1.clear()
                    }
                }
                var compare2 = mutableListOf<RepoContributorsItem>()
                for (i in 0 until result.secondResult.size) {
                    compare2 = contributors2.filter { it.githubId == result.secondResult[i].githubId }.toMutableList()
                    if (compare2.isEmpty()) {
                        contributors2.add(RepoContributorsItem(result.secondResult[i].additions,result.secondResult[i].commits,
                            result.secondResult[i].deletions, result.secondResult[i].githubId))
                    } else {
                        compare2.clear()
                    }
                }
                initSpinner()
            }
        } else {
            if(count<10) {
                count++
                val handler = Handler(Looper.getMainLooper())
                handler.postDelayed({repoContributors(repo1, repo2)}, 5000)
            }
        }
    }

    /*
    spinner에 두 Repository의 github id 리스트 넣는 함수
     */
    private fun initSpinner() {
        if(contributors1.isNotEmpty() && contributors2.isNotEmpty()) {
            val arr1 : MutableList<String> = mutableListOf("선택하세요")
            val arr2 : MutableList<String> = mutableListOf()
            arr1.addAll(contributors1.flatMap { listOf(it.githubId!!) }.toMutableList())
            arr2.addAll(contributors2.flatMap { listOf(it.githubId!!) }.toMutableList())
//            Toast.makeText(requireContext(), "$arr1", Toast.LENGTH_SHORT).show()
            val spinnerAdapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_list_item_1, arr1)
//            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerAdapter.addAll(arr2)
            binding.compareUserSpinner1.adapter = spinnerAdapter
            binding.compareUserSpinner1.setSelection(0)

            binding.compareUserSpinner2.adapter = spinnerAdapter
            binding.compareUserSpinner2.setSelection(0)
//            binding.compareUserSpinner2.invalidate()
            binding.compareUserSpinner1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
//                    if(position > contributors1.size) {
////                        Toast.makeText(requireContext(), "repo2 구성원", Toast.LENGTH_SHORT).show()
//                        val name = contributors2[position-contributors1.size-1].githubId
//                        binding.compareUser1.text = name
//                    } else {
//                        if(position > 0) {
////                            Toast.makeText(requireContext(), "repo1 구성원", Toast.LENGTH_SHORT).show()
//                            val name = contributors1[position-1].githubId
//                            binding.compareUser1.text = name
//                        }
//                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }
            binding.compareUserSpinner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
//                    if(position > contributors1.size) {
////                        Toast.makeText(requireContext(), "repo2 구성원", Toast.LENGTH_SHORT).show()
//                        val name = contributors2[position-contributors1.size-1].githubId
//                        binding.compareUser2.text = name
//                    } else {
//                        if(position > 0) {
////                            Toast.makeText(requireContext(), "repo1 구성원", Toast.LENGTH_SHORT).show()
//                            val name = contributors1[position-1].githubId
//                            binding.compareUser2.text = name
//                        }
//                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }
        }

    }

    /*
    spinner에 선택된 user들을 비교하는 그래프를 그리는 함수
     */
    private fun initGraph(user1: String, user2: String) {
        binding.progressBar.visibility = View.VISIBLE
        binding.userCommitChart.visibility = View.GONE
        binding.userCodeChart.visibility = View.GONE
//        Toast.makeText(requireContext(), "initGraph()", Toast.LENGTH_SHORT).show()
        var user1Cont = contributors1.find { it.githubId == user1 }
        var user2Cont = contributors1.find { it.githubId == user2 }
        if(user1Cont == null) {
            user1Cont = contributors2.find { it.githubId == user1 }
        }
        if(user2Cont == null) {
            user2Cont = contributors2.find { it.githubId == user2 }
        }
        user1Cont!!
        user2Cont!!
        val commitEntries1 = ArrayList<BarEntry>()
        val commitEntries2 = ArrayList<BarEntry>()
        val codeEntries1 = ArrayList<BarEntry>()
        val codeEntries2 = ArrayList<BarEntry>()
//        Toast.makeText(requireContext(), "${user2Cont.commits} ${user2Cont.additions}  ${user2Cont.deletions}", Toast.LENGTH_SHORT).show()
        commitEntries1.add(BarEntry(1.toFloat(), user1Cont.commits!!.toFloat()))
        codeEntries1.add(BarEntry(1.toFloat(), user1Cont.additions!!.toFloat()))
        codeEntries1.add(BarEntry(2.toFloat(), user1Cont.deletions!!.toFloat()))
        commitEntries2.add(BarEntry(1.toFloat(), user2Cont.commits!!.toFloat()))
        codeEntries2.add(BarEntry(1.toFloat(), user2Cont.additions!!.toFloat()))
        codeEntries2.add(BarEntry(2.toFloat(), user2Cont.deletions!!.toFloat()))
//        Toast.makeText(requireContext(), "entries1 : $entries1", Toast.LENGTH_SHORT).show()

        val commitSet1 = BarDataSet(commitEntries1,user1)
        commitSet1.color= Color.rgb(153,255,119)
        commitSet1.apply{
            valueTextSize = 12f
            setDrawValues(true)
            valueFormatter = ScoreCustomFormatter()
        }

        val commitSet2 = BarDataSet(commitEntries2,user2)
        commitSet2.color = Color.BLACK
        commitSet2.apply{
            valueTextSize = 12f
            setDrawValues(true)
            valueFormatter = ScoreCustomFormatter()
        }
        val commitDataSet1 = ArrayList<IBarDataSet>()
        commitDataSet1.add(commitSet1)
        commitDataSet1.add(commitSet2)
        val dataSet2 = ArrayList<IBarDataSet>()
        dataSet2.add(commitSet2)
        val commitData = BarData(commitDataSet1)
        commitData.barWidth = 0.4f
        commitData.groupBars(0.5f, 0.2f, 0f)

        val codeSet1 = BarDataSet(codeEntries1,user1)
        codeSet1.color= Color.rgb(153,255,119)
        codeSet1.apply{
            valueTextSize = 12f
            setDrawValues(true)
            valueFormatter = ScoreCustomFormatter()
        }

        val codeSet2 = BarDataSet(codeEntries2,user2)
        codeSet2.color = Color.BLACK
        codeSet2.apply{
            valueTextSize = 12f
            setDrawValues(true)
            valueFormatter = ScoreCustomFormatter()
        }
        val codeDataSet1 = ArrayList<IBarDataSet>()
        codeDataSet1.add(codeSet1)
        codeDataSet1.add(codeSet2)

        val codeData = BarData(codeDataSet1)
        codeData.barWidth = 0.4f
        codeData.groupBars(0.5f, 0.2f, 0f)

        binding.userCommitChart.apply {
            setFitBars(true)
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
                //setDrawLabels(true) // 값 적는거 허용 (0, 50, 100)
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
                valueFormatter = CommitsFormatter() // X축 라벨값(밑에 표시되는 글자) 바꿔주기 위해 설정
            }
            animateY(500) // 밑에서부터 올라오는 애니매이션 적용
        }

        binding.userCodeChart.apply {
            setFitBars(true)
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
                valueFormatter = CodeFormatter() // X축 라벨값(밑에 표시되는 글자) 바꿔주기 위해 설정
            }
            animateY(500) // 밑에서부터 올라오는 애니매이션 적용
        }
//        binding.userChart.invalidate()
        binding.userCommitChart.data = commitData
        binding.userCodeChart.data = codeData
//        binding.userChart.data.addDataSet(set2)
//        binding.userChart.invalidate()
        binding.progressBar.visibility = View.GONE
        binding.userCommitChart.visibility = View.VISIBLE
        binding.userCodeChart.visibility = View.VISIBLE


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
    class CommitsFormatter() : ValueFormatter() {
        private val days = listOf("commits")
//        private val days = listOf( "additions", "deletions")
        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            return days.getOrNull(value.toInt() - 1) ?: value.toString()
        }
        override fun getFormattedValue(value: Float): String {
            return "" + value.toInt()
        }
    }

    class CodeFormatter() : ValueFormatter() {
        private val days = listOf("additions", "deletions")
        //        private val days = listOf( "additions", "deletions")
        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            return days.getOrNull(value.toInt() - 1) ?: value.toString()
        }
        override fun getFormattedValue(value: Float): String {
            return "" + value.toInt()
        }
    }

    //    막대 위의 커밋수 정수로 변경
    class ScoreCustomFormatter() : ValueFormatter() {
        private val days = listOf("commits", "additions", "deletions")
//        private val days = listOf( "additions", "deletions")
        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            return days.getOrNull(value.toInt() - 1) ?: value.toString().substring(0,2)
        }
        override fun getFormattedValue(value: Float): String {
            return "" + value.toInt()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateUI()
    }

    //activity 구성 이후 화면을 초기화하는 함수
     private fun updateUI() {

         binding.userCompareChoice.setOnClickListener {
             if(binding.compareUserSpinner1.selectedItem.toString() == binding.compareUserSpinner2.selectedItem.toString() ||
                 binding.compareUserSpinner1.selectedItem.toString() == "선택하세요" ||binding.compareUserSpinner2.selectedItem.toString() == "선택하세요") {
                 Toast.makeText(context, "서로 다른 사용자를 선택하세요!!", Toast.LENGTH_SHORT).show()
             } else {
                 initGraph(binding.compareUserSpinner1.selectedItem.toString(), binding.compareUserSpinner2.selectedItem.toString())
             }
         }

         repoContributors(repo1, repo2)


         getActivity()?.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
    }

}