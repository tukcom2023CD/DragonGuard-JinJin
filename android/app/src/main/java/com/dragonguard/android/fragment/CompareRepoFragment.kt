package com.dragonguard.android.fragment

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.marginStart
import androidx.core.view.setMargins
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.dragonguard.android.R
import com.dragonguard.android.databinding.FragmentCompareRepoBinding
import com.dragonguard.android.model.compare.CompareRepoMembersResponseModel
import com.dragonguard.android.model.compare.CompareRepoResponseModel
import com.dragonguard.android.adapters.RepoCompareAdapter
import com.dragonguard.android.model.compare.RepoMembersResult
import com.dragonguard.android.model.compare.RepoStats
import com.dragonguard.android.model.contributors.GitRepoMember
import com.dragonguard.android.viewmodel.Viewmodel
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.LegendEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import kotlin.math.roundToInt
import kotlin.math.roundToLong

//선택한 두 Repository를 비교하기 위한 fragment
class CompareRepoFragment(repoName1: String, repoName2: String, token: String,
                          private val firstRepoMember: List<RepoMembersResult>, private val secondRepoMember: List<RepoMembersResult>) : Fragment() {
    // TODO: Rename and change types of parameters
    private var repo1 = repoName1
    private var repo2 = repoName2
    private var viewmodel = Viewmodel()
    private lateinit var binding : FragmentCompareRepoBinding
    private var count = 0
    private val compareItems = arrayListOf("forks", "closed issues", "open issues", "stars", "subscribers", "watchers", "total commits",
    "max commits", "min commits", "contributors", "average commits", "total additions", "max addtions", "min additions", "adders",
    "average addtions", "total deletions", "max deletions", "min deletions", "deleters", "average deletions", "languages", "average lines")
    private val token = token


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_compare_repo, container, false)
        binding.compareRepoViewmodel = viewmodel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateUI()
    }

    //activity 구성 이후 화면을 초기화하는 함수
    private fun updateUI() {
        binding.repo1User1.clipToOutline = true
        binding.repo1User2.clipToOutline = true
        binding.repo1User3.clipToOutline = true
        binding.repo1User4.clipToOutline = true
        binding.repo2User1.clipToOutline = true
        binding.repo2User2.clipToOutline = true
        binding.repo2User3.clipToOutline = true
        binding.repo2User4.clipToOutline = true
        repoContributors()
    }

    /*비교 전에 멤버를 불러오는 함수
    호출 후 이상유무를 확인하는 함수 호출
     */
    fun repoContributors() {
        val coroutine = CoroutineScope(Dispatchers.Main)
        coroutine.launch {
            val resultDeferred = coroutine.async(Dispatchers.IO) {
                viewmodel.postCompareRepoMembersRequest(repo1, repo2, token)
            }
            val result = resultDeferred.await()
//            Toast.makeText(applicationContext, "result = ${result.size}",Toast.LENGTH_SHORT).show()
            checkContributors(result)
        }
    }

    /*
    멤버를 불러오는 함수의 결과의 이상 유뮤 확인 함수
    이상없으면 비교하는 함수 호출
     */

    fun checkContributors(result: CompareRepoMembersResponseModel) {
        if ((result.first_result != null) && (result.second_result != null)) {
            if (result.first_result.isEmpty()) {
                count++
                val handler = Handler(Looper.getMainLooper())
                handler.postDelayed({repoContributors()}, 2000)
            } else {
                repoCompare()
            }
        } else {
            if(count<10) {
                count++
                val handler = Handler(Looper.getMainLooper())
                handler.postDelayed({repoContributors()}, 2000)
            } else {

            }
        }
    }

    /*
    두 Repository를 비교하는 API를 호출하는 함수
    호출 후 이상유무를 확인하는 함수 호출
     */
    private fun repoCompare() {
        val coroutine = CoroutineScope(Dispatchers.Main)
        coroutine.launch {
            val resultDeferred = coroutine.async(Dispatchers.IO) {
                viewmodel.postCompareRepoRequest(repo1, repo2, token)
            }
            val result = resultDeferred.await()
//            Toast.makeText(applicationContext, "result = ${result.size}",Toast.LENGTH_SHORT).show()
            checkRepos(result)
        }
    }

    /*
    비교하는 API의 결과의 이상유뮤 확인 후
    recyclerview 그리는 함수 호출
     */
    private fun checkRepos(result: CompareRepoResponseModel) {
        if(result.first_repo != null && result.second_repo != null) {
            try {
                result.first_repo.git_repo!!
                result.first_repo.statistics!!
                result.first_repo.languages_stats!!
                result.first_repo.languages!!
                result.second_repo.git_repo!!
                result.second_repo.statistics!!
                result.second_repo.languages_stats!!
                result.second_repo.languages!!
                initRecycler(result)

            } catch (e: Exception) {
                val handler = Handler(Looper.getMainLooper())
                handler.postDelayed({repoCompare()}, 5000)
            }
        } else {
            if(count<10) {
                count++
                val handler = Handler(Looper.getMainLooper())
                handler.postDelayed({repoCompare()}, 5000)
            }
        }
    }

    /*
    두 Repository를 비교하기 위한 표를 그리는 recyclerview
    결과에 문제 없으면 다 그리고 그래프를 그리는 함수 호출
     */
    private fun initRecycler(result: CompareRepoResponseModel) {
        Log.d("initRecycler()", "리사이클러뷰 구현 시작")
        if(result.first_repo!!.languages_stats == null || result.second_repo!!.languages_stats == null) {
            Log.d("initRecycler()", "${result.first_repo.languages_stats} 혹은 ${result.second_repo!!.languages_stats}이 널입니다.")
            count++
            val handler = Handler(Looper.getMainLooper())
            handler.postDelayed({repoCompare()}, 5000)
        } else {
            val repoCompareAdapter = RepoCompareAdapter(result.first_repo!!, result.second_repo!!, compareItems)
            binding.repoCompareList.adapter = repoCompareAdapter
            binding.repoCompareList.layoutManager = LinearLayoutManager(requireContext())
            repoCompareAdapter.notifyDataSetChanged()
            initGraph(result.first_repo, result.second_repo)
        }
    }

    /*
    두 Repository를 비교하기 위한 그래프를 그리는 함수
    가로로 슬라이딩하며 애니메이션 적용함
     */
    private fun initGraph(data1: RepoStats, data2: RepoStats) {
        data1.languages!!
        data1.git_repo!!
        data1.statistics!!
        data1.languages_stats!!
        data2.languages!!
        data2.git_repo!!
        data2.statistics!!
        data2.languages_stats!!
        val sum1 = data1.languages.values.sum()
        val sum2 = data2.languages.values.sum()
        val colors1 = ArrayList<Int>()
        val colors2 = ArrayList<Int>()
        var red1 = 0
        var green1 = 0
        var blue1 = 0
        var red2 = 0
        var green2 = 0
        var blue2 = 0
        var etc1 : Float = 0f
        var etc2 : Float = 0f
        val entries1 = ArrayList<PieEntry>()
        val legendEntry1 = HashMap<String, Int>()
        val legendEntry2 = HashMap<String, Int>()
        val entries2 = ArrayList<PieEntry>()

        data1.languages.forEach {
            if((it.value.toFloat() / sum1)*100 < 7) {
                etc1 += (it.value.toFloat() / sum1)*100
            } else {
                entries1.add(PieEntry((it.value.toFloat() / sum1)*100, it.key))
                red1 = (Math.random() * 255).toInt()
                green1 = (Math.random() * 255).toInt()
                blue1 = (Math.random() * 255).toInt()
                colors1.add(Color.rgb(red1, green1, blue1))
                legendEntry1[it.key] = Color.rgb(red1, green1, blue1)
            }
        }
        if(etc1 != 0f) {
            entries1.add(PieEntry(etc1, "etc"))
            colors1.add(Color.BLACK)
            legendEntry1["etc"] = Color.BLACK
        }

        legendEntry1.forEach {
            val linear = LinearLayout(requireContext())
            linear.orientation = LinearLayout.HORIZONTAL
            linear.gravity = Gravity.CENTER
            val param = LinearLayout.LayoutParams(30,30)
            param.setMargins(5, 0, 0, 0)
            val color = View(requireContext())
            color.setBackgroundColor(it.value)
            color.layoutParams = param
            val text = TextView(requireContext())
            text.text = it.key
            text.textSize = 11f
            text.setTextColor(Color.BLACK)
            val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT)
            params.setMargins(5, 0, 10, 0)
            text.layoutParams = params
            linear.addView(color)
            linear.addView(text)
            binding.repo1Legend.addView(linear)
        }

        Log.d("entry", "legendEntry $legendEntry1")
        val dataSet1 = PieDataSet(entries1, data1.git_repo.full_name)
        dataSet1.label = null
        dataSet1.setDrawValues(true)
        dataSet1.valueTextSize = 12f
        dataSet1.colors = colors1
        dataSet1.valueFormatter = ScoreCustomFormatter()
        dataSet1.valueTextColor = Color.WHITE
        val firstData = PieData(dataSet1)
        binding.repo1Language.setTouchEnabled(true)
        binding.repo1Language.description.isEnabled = false
        binding.repo1Language.data = firstData
        binding.repo1Language.setDrawEntryLabels(true)
        binding.repo1Language.setEntryLabelColor(Color.BLACK)
        binding.repo1Language.legend.isEnabled = false
        binding.repo1Language.invalidate()
        binding.repo1Language.visibility = View.GONE
        binding.repo1Language.visibility = View.VISIBLE

        data2.languages!!.forEach {
            if((it.value.toFloat() / sum2)*100<7) {
                etc2 += (it.value.toFloat() / sum2)*100
            } else {
                entries2.add(PieEntry((it.value.toFloat() / sum2)*100, it.key))
                red2 = (Math.random() * 255).toInt()
                green2 = (Math.random() * 255).toInt()
                blue2 = (Math.random() * 255).toInt()
                colors2.add(Color.rgb(red2, green2, blue2))
                legendEntry2[it.key] = Color.rgb(red2, green2, blue2)
            }
        }
        if(etc2 != 0f) {
            entries2.add(PieEntry(etc2, "etc"))
            colors2.add(Color.BLACK)
            legendEntry2["etc"] = Color.BLACK
        }

        val dataSet2 = PieDataSet(entries2, data2.git_repo!!.full_name)
        dataSet2.label = null
        dataSet2.colors = colors2
        dataSet2.setDrawValues(true)
        dataSet2.valueFormatter = ScoreCustomFormatter()
        dataSet2.valueTextColor = Color.WHITE
        dataSet2.valueTextSize = 12f

        legendEntry2.forEach {
            val linear = LinearLayout(requireContext())
            linear.orientation = LinearLayout.HORIZONTAL
            linear.gravity = Gravity.CENTER
            val param = LinearLayout.LayoutParams(30,30)
            param.setMargins(5, 0, 0, 0)
            val color = View(requireContext())
            color.setBackgroundColor(it.value)
            color.layoutParams = param
            val text = TextView(requireContext())
            text.text = it.key
            text.textSize = 11f
            val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT)
            params.setMargins(5, 0, 10, 0)
            text.layoutParams = params
            linear.addView(color)
            linear.addView(text)
            text.setTextColor(Color.BLACK)
            binding.repo2Legend.addView(linear)
        }

        val secondData = PieData(dataSet2)
        binding.repo2Language.setTouchEnabled(true)
        binding.repo2Language.description.isEnabled = false
        binding.repo2Language.data = secondData
        binding.repo2Language.legend.isEnabled = false
        binding.repo2Language.setDrawEntryLabels(true)
        binding.repo2Language.setEntryLabelColor(Color.BLACK)
        binding.repo2Language.invalidate()
        binding.repo2Language.visibility = View.GONE
        binding.repo2Language.visibility = View.VISIBLE
        initProfiles()
    }

    private fun initProfiles() {
        Log.d("frist", "first : $firstRepoMember")
        Log.d("second", "first : $secondRepoMember")
        when(firstRepoMember.size) {
            0 -> {
                binding.repo1User1.visibility = View.INVISIBLE
                binding.repo1User2.visibility = View.INVISIBLE
                binding.repo1User3.visibility = View.INVISIBLE
                binding.repo1User4.visibility = View.INVISIBLE
            }
            1 -> {
                adaptProfile(1, firstRepoMember[0].profile_url)
                binding.repo1User2.visibility = View.INVISIBLE
                binding.repo1User3.visibility = View.INVISIBLE
                binding.repo1User4.visibility = View.INVISIBLE
            }
            2 -> {
                adaptProfile(1, firstRepoMember[0].profile_url)
                adaptProfile(2, firstRepoMember[1].profile_url)
                binding.repo1User3.visibility = View.INVISIBLE
                binding.repo1User4.visibility = View.INVISIBLE
            }
            3 -> {
                adaptProfile(1, firstRepoMember[0].profile_url)
                adaptProfile(2, firstRepoMember[1].profile_url)
                adaptProfile(3, firstRepoMember[2].profile_url)
                binding.repo1User4.visibility = View.INVISIBLE
            }
            else -> {
                adaptProfile(1, firstRepoMember[0].profile_url)
                adaptProfile(2, firstRepoMember[1].profile_url)
                adaptProfile(3, firstRepoMember[2].profile_url)
                adaptProfile(4, firstRepoMember[3].profile_url)
            }

        }

        when(secondRepoMember.size) {
            0 -> {
                binding.repo2User1.visibility = View.INVISIBLE
                binding.repo2User2.visibility = View.INVISIBLE
                binding.repo2User3.visibility = View.INVISIBLE
                binding.repo2User4.visibility = View.INVISIBLE
            }
            1 -> {
                adaptProfile(5, secondRepoMember[0].profile_url)
                binding.repo2User2.visibility = View.INVISIBLE
                binding.repo2User3.visibility = View.INVISIBLE
                binding.repo2User4.visibility = View.INVISIBLE
            }
            2 -> {
                adaptProfile(5, secondRepoMember[0].profile_url)
                adaptProfile(6, secondRepoMember[1].profile_url)
                binding.repo2User3.visibility = View.INVISIBLE
                binding.repo2User4.visibility = View.INVISIBLE
            }
            3 -> {
                adaptProfile(5, secondRepoMember[0].profile_url)
                adaptProfile(6, secondRepoMember[1].profile_url)
                adaptProfile(7, secondRepoMember[2].profile_url)
                binding.repo2User4.visibility = View.INVISIBLE
            }
            else -> {
                adaptProfile(5, secondRepoMember[0].profile_url)
                adaptProfile(6, secondRepoMember[1].profile_url)
                adaptProfile(7, secondRepoMember[2].profile_url)
                adaptProfile(8, secondRepoMember[3].profile_url)
            }
        }

        binding.rankingLottie.pauseAnimation()
        binding.rankingLottie.visibility = View.GONE
        binding.repoCompareList.visibility = View.VISIBLE
        binding.repo1Name.text = repo1
        binding.repo2Name.text = repo2
        binding.compareRepoFrame.visibility = View.VISIBLE
    }

    private fun adaptProfile(order: Int, url: String?) {
        url?.let{
            when(order) {
                1 -> {
                    Glide.with(binding.repo1User1).load(url)
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(binding.repo1User1)
                    binding.compareRepoFrame.visibility = View.VISIBLE
                }
                2 -> {
                    Glide.with(binding.repo1User2).load(url)
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(binding.repo1User2)
                    binding.compareRepoFrame.visibility = View.VISIBLE
                }
                3 -> {
                    Glide.with(binding.repo1User3).load(url)
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(binding.repo1User3)
                    binding.compareRepoFrame.visibility = View.VISIBLE
                }
                4 -> {
                    Glide.with(binding.repo1User4).load(url)
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(binding.repo1User4)
                    binding.compareRepoFrame.visibility = View.VISIBLE
                }
                5 -> {
                    Glide.with(binding.repo2User1).load(url)
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(binding.repo2User1)
                    binding.compareRepoFrame.visibility = View.VISIBLE
                }
                6 -> {
                    Glide.with(binding.repo2User2).load(url)
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(binding.repo2User2)
                    binding.compareRepoFrame.visibility = View.VISIBLE
                }
                7 -> {
                    Glide.with(binding.repo2User3).load(url)
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(binding.repo2User3)
                    binding.compareRepoFrame.visibility = View.VISIBLE
                }
                8 -> {
                    Glide.with(binding.repo2User4).load(url)
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(binding.repo2User4)
                    binding.compareRepoFrame.visibility = View.VISIBLE
                }
            }
        }
    }

    class ScoreCustomFormatter() : ValueFormatter() {
        override fun getFormattedValue(value: Float): String {
            return "${DecimalFormat("#.##").format(value)}%"
        }
    }

}