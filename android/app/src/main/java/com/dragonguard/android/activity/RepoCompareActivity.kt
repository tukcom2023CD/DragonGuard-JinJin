package com.dragonguard.android.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import com.dragonguard.android.R
import com.dragonguard.android.databinding.ActivityRepoCompareBinding
import com.dragonguard.android.viewmodel.Viewmodel

class RepoCompareActivity : AppCompatActivity() {
    private val activityResultLauncher : ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) {
        val compareRepo = it.data
        val compareRepoName = compareRepo!!.getStringExtra("repo_name")
        if(it.resultCode == 0 ) {
            binding.repoCompare1.text = compareRepoName

        } else if (it.resultCode == 1) {
            binding.repoCompare2.text = compareRepoName
        }
    }
    private lateinit var binding: ActivityRepoCompareBinding
    private var viewmodel = Viewmodel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_repo_compare)
        binding.repoCompareViewModel = viewmodel

        binding.repoCompare1.setOnClickListener {
            val intent = Intent(applicationContext, CompareSearchActivity::class.java)
            intent.putExtra("count", 0)
            activityResultLauncher.launch(intent)
        }

        binding.repoCompare2.setOnClickListener {
            val intent = Intent(applicationContext, CompareSearchActivity::class.java)
            intent.putExtra("count", 1)
            activityResultLauncher.launch(intent)
        }

        binding.repoChoose.setOnClickListener {
            if(binding.repoCompare1.text.isNullOrBlank() || binding.repoCompare2.text.isNullOrBlank()) {

            } else {
//                val intent = Intent(applicationContext, ::class.java)
            }
        }
    }
}