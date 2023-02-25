package com.dragonguard.android.fragment

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.constraint.ConstraintLayout
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.viewpager2.widget.ViewPager2
import com.dragonguard.android.R
import com.dragonguard.android.databinding.FragmentCompareRepoBinding
import com.dragonguard.android.model.CompareRepoMembersResponseModel
import com.dragonguard.android.model.CompareRepoResponseModel
import com.dragonguard.android.model.RepoContributorsItem
import com.dragonguard.android.recycleradapter.RepoCompareChartAdapter
import com.dragonguard.android.viewmodel.Viewmodel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.lang.invoke.ConstantCallSite


class CompareRepoFragment(repoName1: String, repoName2: String) : Fragment() {
    // TODO: Rename and change types of parameters
    private var repo1 = repoName1
    private var repo2 = repoName2
    private var viewmodel = Viewmodel()
    private lateinit var binding : FragmentCompareRepoBinding
    private var count = 0
    private val MIN_SCALE = 0.85f
    private val MIN_ALPHA = 0.5f


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

    private fun updateUI() {
        repoContributors()
    }

    fun repoContributors() {
        val coroutine = CoroutineScope(Dispatchers.Main)
        coroutine.launch {
            val resultDeferred = coroutine.async(Dispatchers.IO) {
                viewmodel.postCompareRepoMembersRequest(repo1, repo2)
            }
            val result = resultDeferred.await()
//            Toast.makeText(applicationContext, "result = ${result.size}",Toast.LENGTH_SHORT).show()
            checkContributors(result)
        }
    }

    fun checkContributors(result: CompareRepoMembersResponseModel) {
        if ((result.firstResult != null) && (result.secondResult != null)) {
            if (result.firstResult.isEmpty()) {
                count++
                val handler = Handler(Looper.getMainLooper())
                handler.postDelayed({repoContributors()}, 5000)
            } else {
                val name1 = repo1.split("/","_","-")
                val name2 = repo2.split("/","_","-")
                if(name1.size > 2) {
                    binding.compareRepo1.text = "${name1[name1.lastIndex-1]}\n${name1.last()}"
                } else {
                    binding.compareRepo1.text = name1.last()
                }
                if(name2.size > 2) {
                    binding.compareRepo2.text = "${name2[name2.lastIndex-1]}\n${name2.last()}"
                } else {
                    binding.compareRepo2.text = name2.last()
                }
                count = 0
                repoCompare()
            }
        } else {
            if(count<10) {
                count++
                val handler = Handler(Looper.getMainLooper())
                handler.postDelayed({repoContributors()}, 5000)
            }
        }
    }

    private fun repoCompare() {
        val coroutine = CoroutineScope(Dispatchers.Main)
        coroutine.launch {
            val resultDeferred = coroutine.async(Dispatchers.IO) {
                viewmodel.postCompareRepoRequest(repo1, repo2)
            }
            val result = resultDeferred.await()
//            Toast.makeText(applicationContext, "result = ${result.size}",Toast.LENGTH_SHORT).show()
            checkRepos(result)
        }
    }

    private fun checkRepos(result: CompareRepoResponseModel) {
        if(result.firstRepo != null && result.secondRepo != null) {
            if(result.firstRepo.languages != null && result.secondRepo.languages!= null) {
//                Toast.makeText(requireContext(), "${result.firstRepo.gitRepo.full_name}", Toast.LENGTH_SHORT).show()
                Log.d("firstRepo.gitRepo", "first language : ${"${result.firstRepo.languages}"}")
                Log.d("second.gitRepo", "second language : ${"${result.secondRepo.languages}"}")
                initGraph(result)
            }
        } else {
            if(count<10) {
                count++
                val handler = Handler(Looper.getMainLooper())
                handler.postDelayed({repoCompare()}, 5000)
            }
        }
    }

    private fun initGraph(result: CompareRepoResponseModel) {
        binding.repoCompareChartViewpager.adapter = RepoCompareChartAdapter(result.firstRepo!!, result.secondRepo!!)
        binding.repoCompareChartViewpager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        binding.repoCompareChartViewpager.setPageTransformer(ZoomOutPageTransformer())
        binding.repoCompareChartViewpager.isUserInputEnabled = true

    }
    inner class ZoomOutPageTransformer : ViewPager2.PageTransformer {
        override fun transformPage(view: View, position: Float) {
            view.apply {
                val pageWidth = width
                val pageHeight = height
                when {
                    position < -1 -> { // [-Infinity,-1)
                        // This page is way off-screen to the left.
                        alpha = 0f
                    }
                    position <= 1 -> { // [-1,1]
                        // Modify the default slide transition to shrink the page as well
                        val scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position))
                        val vertMargin = pageHeight * (1 - scaleFactor) / 2
                        val horzMargin = pageWidth * (1 - scaleFactor) / 2
                        translationX = if (position < 0) {
                            horzMargin - vertMargin / 2
                        } else {
                            horzMargin + vertMargin / 2
                        }

                        // Scale the page down (between MIN_SCALE and 1)
                        scaleX = scaleFactor
                        scaleY = scaleFactor

                        // Fade the page relative to its size.
                        alpha = (MIN_ALPHA +
                                (((scaleFactor - MIN_SCALE) / (1 - MIN_SCALE)) * (1 - MIN_ALPHA)))
                    }
                    else -> { // (1,+Infinity]
                        // This page is way off-screen to the right.
                        alpha = 0f
                    }
                }
            }
        }
    }

}