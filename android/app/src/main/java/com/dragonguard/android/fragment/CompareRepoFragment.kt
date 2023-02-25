package com.dragonguard.android.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.dragonguard.android.R
import com.dragonguard.android.databinding.FragmentCompareRepoBinding
import com.dragonguard.android.model.CompareRepoMembersResponseModel
import com.dragonguard.android.model.CompareRepoResponseModel
import com.dragonguard.android.model.RepoContributorsItem
import com.dragonguard.android.viewmodel.Viewmodel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class CompareRepoFragment(repoName1: String, repoName2: String) : Fragment() {
    // TODO: Rename and change types of parameters
    private var repo1 = repoName1
    private var repo2 = repoName2
    private var viewmodel = Viewmodel()
    private lateinit var binding : FragmentCompareRepoBinding
    private var count = 0


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
                binding.compareRepo1.text = repo1.split("/").last()
                binding.compareRepo2.text = repo2.split("/").last()
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
            if(result.firstRepo.gitRepo == null || result.firstRepo.languages.isNullOrEmpty() || result.firstRepo.languagesStat == null ||
                result.firstRepo.statistics == null || result.secondRepo.gitRepo == null || result.secondRepo.languages.isNullOrEmpty() ||
                    result.secondRepo.languagesStat == null || result.secondRepo.statistics == null){
                if(count<10) {
                    count++
                    val handler = Handler(Looper.getMainLooper())
                    handler.postDelayed({repoCompare()}, 5000)
                }
            } else {
                Toast.makeText(requireContext(), "${result.secondRepo.languagesStat.average}", Toast.LENGTH_SHORT).show()
//                Log.d("compare", "${result.secondRepo.languagesStat.average}")
            }
        } else {
            if(count<10) {
                count++
                val handler = Handler(Looper.getMainLooper())
                handler.postDelayed({repoCompare()}, 5000)
            }
        }
    }
}