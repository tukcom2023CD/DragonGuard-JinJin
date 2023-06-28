package com.dragonguard.android.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import com.dragonguard.android.R
import com.dragonguard.android.activity.compare.RepoChooseActivity
import com.dragonguard.android.activity.compare.RepoCompareActivity
import com.dragonguard.android.activity.search.SearchActivity
import com.dragonguard.android.databinding.FragmentCompareSearchBinding

class CompareSearchFragment(private val token: String) : Fragment() {
    private lateinit var binding: FragmentCompareSearchBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_compare_search, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            val repoName = it.getString("repoName")
            val number = it.getInt("number")
            when(number) {
                1 -> {
                    binding.repoCompare1.text = repoName
                }
                2 -> {
                    binding.repoCompare2.text = repoName
                }
            }
        }
        binding.repoCompare1.setOnClickListener {
            val intent = Intent(activity, SearchActivity::class.java)
            intent.putExtra("token", token)
            intent.putExtra("count", 1)
            resultLauncher.launch(intent)
        }
        binding.repoCompare2.setOnClickListener {
            val intent = Intent(activity, SearchActivity::class.java)
            intent.putExtra("token", token)
            intent.putExtra("count", 2)
            resultLauncher.launch(intent)
        }
        binding.repoChoose.setOnClickListener {
            if(binding.repoCompare1.text.isNullOrBlank() || binding.repoCompare2.text.isNullOrBlank() ||
                binding.repoCompare1.text == "Repository 1" || binding.repoCompare2.text == "Repository 2") {
                Toast.makeText(requireContext(), "비교할 Repository를 선택해 주세요!!", Toast.LENGTH_SHORT).show()
            } else {
                if(binding.repoCompare1.text.toString() != binding.repoCompare2.text.toString()) {
                    val intent = Intent(requireContext(), RepoCompareActivity::class.java)
                    intent.putExtra("repo1", binding.repoCompare1.text.toString())
                    intent.putExtra("repo2", binding.repoCompare2.text.toString())
                    intent.putExtra("token", token)
                    startActivity(intent)
                } else {
                    Toast.makeText(requireContext(),"서로 다른 Repository를 선택해 주세요!!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        when(result.resultCode) {
            1 -> {
                binding.repoCompare1.text = result.data?.getStringExtra("repoName")
            }
            2 -> {
                binding.repoCompare2.text = result.data?.getStringExtra("repoName")
            }
        }
    }
}