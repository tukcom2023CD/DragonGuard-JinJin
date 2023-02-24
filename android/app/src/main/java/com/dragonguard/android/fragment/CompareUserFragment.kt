package com.dragonguard.android.fragment

import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.dragonguard.android.R
import com.dragonguard.android.activity.MainActivity
import com.dragonguard.android.databinding.FragmentCompareUserBinding
import com.dragonguard.android.model.CompareRepoResponseModel
import com.dragonguard.android.model.FirstResult
import com.dragonguard.android.model.RepoContributorsItem
import com.dragonguard.android.model.SecondResult
import com.dragonguard.android.recycleradapter.ContributorsAdapter
import com.dragonguard.android.viewmodel.Viewmodel
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CompareUserFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CompareUserFragment(repoName1: String, repoName2: String) : Fragment() {

    private var repo1 = repoName1
    private var repo2 = repoName2
    private var contributors1 = ArrayList<RepoContributorsItem>()
    private var contributors2 = ArrayList<RepoContributorsItem>()
    private var count = 0
    private lateinit var binding : FragmentCompareUserBinding
    private var viewmodel = Viewmodel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_compare_user, container, false)
        binding.compareUserViewmodel = viewmodel

        return binding.root
    }

    fun repoContributors(repoName1: String, repoName2: String) {
        val coroutine = CoroutineScope(Dispatchers.Main)
        coroutine.launch {
            val resultDeferred = coroutine.async(Dispatchers.IO) {
                viewmodel.postCompareRepoMembersRequest(repoName1, repoName2)
            }
            val result = resultDeferred.await()
//            Toast.makeText(applicationContext, "result = ${result.size}",Toast.LENGTH_SHORT).show()
            checkContributors(result)
        }
    }

    //    검색한 결과가 잘 왔는지 확인
    fun checkContributors(result: CompareRepoResponseModel) {
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
                        contributors1.add(RepoContributorsItem(result.firstResult[i].additions,result.firstResult[i].commits,result.firstResult[i].deletions, result.firstResult[i].githubId))
                    } else {
                        compare1.clear()
                    }
                }
                var compare2 = mutableListOf<RepoContributorsItem>()
                for (i in 0 until result.secondResult.size) {
                    compare2 = contributors2.filter { it.githubId == result.secondResult[i].githubId }.toMutableList()
                    if (compare2.isEmpty()) {
                        contributors2.add(RepoContributorsItem(result.secondResult[i].additions,result.secondResult[i].commits,result.secondResult[i].deletions, result.secondResult[i].githubId))
                    } else {
                        compare2.clear()
                    }
                }
                initRecycler()
            }
        } else {
            if(count<10) {
                val handler = Handler(Looper.getMainLooper())
                handler.postDelayed({repoContributors(repo1, repo2)}, 5000)
            }
        }
    }


    private fun initRecycler() {
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
                    if(position > contributors1.size) {
//                        Toast.makeText(requireContext(), "repo2 구성원", Toast.LENGTH_SHORT).show()
                        val name = contributors2[position-contributors1.size-1].githubId
                        binding.compareUser1.text = name
                    } else {
                        if(position > 0) {
//                            Toast.makeText(requireContext(), "repo1 구성원", Toast.LENGTH_SHORT).show()
                            val name = contributors1[position-1].githubId
                            binding.compareUser1.text = name
                        }
                    }
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
                    if(position > contributors1.size) {
//                        Toast.makeText(requireContext(), "repo2 구성원", Toast.LENGTH_SHORT).show()
                        val name = contributors2[position-contributors1.size-1].githubId
                        binding.compareUser2.text = name
                    } else {
                        if(position > 0) {
//                            Toast.makeText(requireContext(), "repo1 구성원", Toast.LENGTH_SHORT).show()
                            val name = contributors1[position-1].githubId
                            binding.compareUser2.text = name
                        }
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }
        }

    }

    private fun initGraph(user1: String, user2: String) {
        binding.progressBar.visibility = View.VISIBLE
        binding.userChart.visibility = View.GONE
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
        val entries1 = ArrayList<RadarEntry>()
        val entries2 = ArrayList<RadarEntry>()
//        Toast.makeText(requireContext(), "${user2Cont.commits} ${user2Cont.additions}  ${user2Cont.deletions}", Toast.LENGTH_SHORT).show()
        entries1.add(RadarEntry(user1Cont.commits!!.toFloat()))
        entries1.add(RadarEntry(user1Cont.additions!!.toFloat()))
        entries1.add(RadarEntry(user1Cont.deletions!!.toFloat()))
        entries2.add(RadarEntry(user2Cont.commits!!.toFloat()))
        entries2.add(RadarEntry(user2Cont.additions!!.toFloat()))
        entries2.add(RadarEntry(user2Cont.deletions!!.toFloat()))
//        Toast.makeText(requireContext(), "entries1 : $entries1", Toast.LENGTH_SHORT).show()

        val set1 = RadarDataSet(entries1,user1)
        set1.color= Color.rgb(153,255,119)
        set1.apply{
            valueTextSize = 12f
            setDrawValues(true)
            valueFormatter = ScoreCustomFormatter()
        }

        val set2 = RadarDataSet(entries2,user2)
        set2.color = Color.BLACK
        set2.apply{
            valueTextSize = 12f
            setDrawValues(true)
            valueFormatter = ScoreCustomFormatter()
        }
        val dataSet1 = ArrayList<IRadarDataSet>()
        dataSet1.add(set1)
        val dataSet2 = ArrayList<IRadarDataSet>()
        dataSet2.add(set2)
        val data = RadarData(dataSet1)
        data.addDataSet(set2)

        binding.userChart.apply {
            setTouchEnabled(false) // 차트 터치 막기
            description.isEnabled = false // 그래프 오른쪽 하단에 라벨 표시
            legend.isEnabled = true // 차트 범례 설정(legend object chart)
            xAxis.apply {
                isEnabled = true
                position = XAxis.XAxisPosition.BOTTOM //X축을 아래에다가 둔다.
                granularity = 1f
                setDrawAxisLine(true) // 축 그림
                setDrawGridLines(false)
                valueFormatter = MyXAxisFormatter()
                textColor = ContextCompat.getColor(context, R.color.black) //라벨 색상
                textSize = 12f // 텍스트 크기
            }
        }
//        binding.userChart.invalidate()
        binding.userChart.data = data
//        binding.userChart.data.addDataSet(set2)
//        binding.userChart.invalidate()
        binding.progressBar.visibility = View.GONE
        binding.userChart.visibility = View.VISIBLE


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
    class MyXAxisFormatter() : ValueFormatter() {
        private val days = listOf("commits", "additions", "deletions")
//        private val days = listOf( "additions", "deletions")
        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            return days.getOrNull(value.toInt()) ?: value.toString()
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
     fun updateUI() {

         binding.userCompareChoice.setOnClickListener {
             if(binding.compareUser1.text == binding.compareUser2.text || binding.compareUser2.text.isNullOrBlank() ||binding.compareUser1.text.isNullOrBlank()) {
                 Toast.makeText(context, "서로 다른 사용자를 선택하세요!!", Toast.LENGTH_SHORT).show()
             } else {
                 initGraph(binding.compareUser1.text.toString(), binding.compareUser2.text.toString())
             }
         }

         repoContributors(repo1, repo2)


         getActivity()?.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
    }

}