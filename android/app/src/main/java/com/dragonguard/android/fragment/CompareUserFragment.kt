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
import com.dragonguard.android.model.RepoContributorsItem
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

        binding.userCompareChoice.setOnClickListener {
            Log.d("tag", "제발")
        }

        repoContributors(repo1, 1)
        repoContributors(repo2, 2)

        getActivity()?.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        return inflater.inflate(R.layout.fragment_compare_user, container, false)
    }

    fun repoContributors(repoName: String, order: Int) {
        val coroutine = CoroutineScope(Dispatchers.Main)
        coroutine.launch {
            val resultDeferred = coroutine.async(Dispatchers.IO) {
                viewmodel.getRepoContributors(repoName)
            }
            val result = resultDeferred.await()
//            Toast.makeText(applicationContext, "result = ${result.size}",Toast.LENGTH_SHORT).show()
            checkContributors(result, repoName, order)
        }
    }

    //    검색한 결과가 잘 왔는지 확인
    fun checkContributors(result: ArrayList<RepoContributorsItem>, repoName: String, order: Int) {
        if (result.isNotEmpty()) {
            if (result[0].additions == null) {
                val handler = Handler(Looper.getMainLooper())
                handler.postDelayed({ repoContributors(repoName, order) }, 3000)

            } else {
                if(order == 1) {
                    for (i in 0 until result.size) {
                        val compare = contributors1.filter { it.githubId == result[i].githubId }
                        if (compare.isEmpty()) {
                            contributors1.add(result[i])
                        }
                    }
                } else if (order == 2) {
                    for (i in 0 until result.size) {
                        val compare = contributors2.filter { it.githubId == result[i].githubId }
                        if (compare.isEmpty()) {
                            contributors2.add(result[i])
                        }
                    }
                    initRecycler()
                }
            }
        } else {
            if (count<10) {
                count++
                val handler = Handler(Looper.getMainLooper())
                handler.postDelayed({ repoContributors(repoName, order) }, 3000)
            } else {
//                binding.progressBar.visibility = View.GONE
            }
        }
    }

    private fun initRecycler() {
        if(contributors1.isNotEmpty() && contributors2.isNotEmpty()) {
            val arr1 : MutableList<String> = mutableListOf("선택하세요")
            val arr2 : MutableList<String> = mutableListOf("선택하세요")
            arr1.addAll(contributors1.flatMap { listOf(it.githubId!!) }.toMutableList())
            arr2.addAll(contributors2.flatMap { listOf(it.githubId!!) }.toMutableList())
            Toast.makeText(requireContext(), "$arr1", Toast.LENGTH_SHORT).show()
            val spinnerAdapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_list_item_1, arr1)
//            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//            spinnerAdapter.addAll(arr2)
            binding.compareUserSpinner1.adapter = spinnerAdapter
            binding.compareUserSpinner1.setSelection(0)

//            binding.compareUserSpinner2.adapter = spinnerAdapter
//            binding.compareUserSpinner2.setSelection(0)
//            binding.compareUserSpinner2.invalidate()
            binding.compareUserSpinner1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if(position >= contributors1.size) {
                        val name = contributors2[position-contributors1.size].githubId
                        binding.compareUser1.text = name
                    } else {
                        val name = contributors1[position].githubId
                        binding.compareUser1.text = name
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }
//            binding.compareUserSpinner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//                override fun onItemSelected(
//                    parent: AdapterView<*>?,
//                    view: View?,
//                    position: Int,
//                    id: Long
//                ) {
//                    if(position >= contributors1.size) {
//                        val name = contributors2[position-contributors1.size].githubId
//                        binding.compareUser2.text = name
//                    } else {
//                        val name = contributors1[position].githubId
//                        binding.compareUser2.text = name
//                    }
//                }
//
//                override fun onNothingSelected(parent: AdapterView<*>?) {
//                }
//            }
            initGraph()
        }

    }

    private fun initGraph() {
//        Toast.makeText(requireContext(), "initGraph()", Toast.LENGTH_SHORT).show()
        val entries1 = ArrayList<RadarEntry>()
        val entries2 = ArrayList<RadarEntry>()
        entries1.add(RadarEntry(contributors1[0].commits!!.toFloat()))
        entries1.add(RadarEntry(contributors1[0].additions!!.toFloat()))
        entries1.add(RadarEntry(contributors1[0].deletions!!.toFloat()))
        entries2.add(RadarEntry(contributors2[0].commits!!.toFloat()))
        entries2.add(RadarEntry(contributors2[0].additions!!.toFloat()))
        entries2.add(RadarEntry(contributors2[0].deletions!!.toFloat()))
        Toast.makeText(requireContext(), "entries1 : $entries1", Toast.LENGTH_SHORT).show()

        val set1 = RadarDataSet(entries1,"DataSet1")
        set1.color= Color.rgb(153,255,51)
        set1.apply{
            valueTextSize = 12f
            setDrawValues(true)
            valueFormatter = ScoreCustomFormatter()
        }

        val set2 = RadarDataSet(entries1,"DataSet2")
        set2.color = Color.rgb(0, 0, 0)
        set2.apply{
            valueTextSize = 12f
            setDrawValues(true)
            valueFormatter = ScoreCustomFormatter()
        }
        val dataSet1 = ArrayList<IRadarDataSet>()
        dataSet1.add(set1)
        val data = RadarData(dataSet1)
        data.addDataSet(set2)

        binding.userChart.apply {
            setTouchEnabled(false) // 차트 터치 막기
            description.isEnabled = false // 그래프 오른쪽 하단에 라벨 표시
            legend.isEnabled = true // 차트 범례 설정(legend object chart)
            xAxis.apply {
                isEnabled = true
                position = XAxis.XAxisPosition.BOTTOM //X축을 아래에다가 둔다.
                granularity = 1f // 1 단위만큼 간격 두기
                setDrawAxisLine(true) // 축 그림
                valueFormatter = MyXAxisFormatter()
                textColor = ContextCompat.getColor(context, R.color.black) //라벨 색상
                textSize = 12f // 텍스트 크기
            }
        }
//        binding.userChart.invalidate()
        binding.userChart.data = data
        binding.userChart.invalidate()
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
        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            return days.getOrNull(value.toInt() - 1) ?: value.toString().substring(0,2)
        }
        override fun getFormattedValue(value: Float): String {
            return "" + value.toInt()
        }
    }

    override fun onResume() {
        super.onResume()
        updateUI()
    }

     fun updateUI() {
        binding.userCompareChoice.setOnClickListener {
            Log.d("tag", "제발")
        }

        repoContributors(repo1, 1)
        repoContributors(repo2, 2)
    }

}