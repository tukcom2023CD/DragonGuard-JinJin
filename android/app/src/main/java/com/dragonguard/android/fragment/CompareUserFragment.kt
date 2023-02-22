package com.dragonguard.android.fragment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.dragonguard.android.R
import com.dragonguard.android.activity.MainActivity
import com.dragonguard.android.databinding.FragmentCompareUserBinding
import com.dragonguard.android.model.RepoContributorsItem
import com.dragonguard.android.recycleradapter.ContributorsAdapter
import com.dragonguard.android.viewmodel.Viewmodel
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
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var repo1 = repoName1
    private var repo2 = repoName2
    private var contributors1 = ArrayList<RepoContributorsItem>()
    private var contributors2 = ArrayList<RepoContributorsItem>()
    private var count = 0
    private lateinit var binding : FragmentCompareUserBinding
    private var viewmodel = Viewmodel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_compare_user, container, false)
        binding.compareUserViewmodel = viewmodel

        repoContributors(repo1, 1)
        repoContributors(repo2, 2)
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
            Toast.makeText(requireContext(), "contributors1 : $arr1 contributors1 : $arr2", Toast.LENGTH_SHORT).show()
            val spinnerAdapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_list_item_1, arr1)
//            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//            spinnerAdapter.addAll(arr2)
            binding.compareUserSpinner1.adapter = spinnerAdapter
            binding.compareUserSpinner2.adapter = spinnerAdapter
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
            binding.compareUserSpinner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if(position >= contributors1.size) {
                        val name = contributors2[position-contributors1.size].githubId
                        binding.compareUser2.text = name
                    } else {
                        val name = contributors1[position].githubId
                        binding.compareUser2.text = name
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }
        }
//        binding.repoContributors.setItemViewCacheSize(contributors.size)
////        Toast.makeText(applicationContext, "리사이클러뷰 시작", Toast.LENGTH_SHORT).show()
////        Toast.makeText(applicationContext, "contributors 수 : ${contributors.size}", Toast.LENGTH_SHORT).show()
//        contributorsAdapter = ContributorsAdapter(contributors, this, colorsets)
//        binding.repoContributors.adapter = contributorsAdapter
//        binding.repoContributors.layoutManager = LinearLayoutManager(this)
//        binding.repoContributors.visibility = View.VISIBLE
//        binding.progressBar.visibility = View.GONE
    }

}