package com.dragonguard.android.activity.compare

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import com.dragonguard.android.R
import com.dragonguard.android.activity.basic.MainActivity
import com.dragonguard.android.databinding.ActivityRepoChooseBinding
import com.dragonguard.android.viewmodel.Viewmodel

class RepoChooseActivity : AppCompatActivity() {
    private val activityResultLauncher : ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) {
        val compareRepo = it.data
        try {
            val compareRepoName = compareRepo!!.getStringExtra("repo_name")
            if(it.resultCode == 0 ) {
                binding.repoCompare1.text = compareRepoName

            } else if (it.resultCode == 1) {
                binding.repoCompare2.text = compareRepoName
            }
        } catch (e: Exception) {

        }
    }
    private lateinit var binding: ActivityRepoChooseBinding
    private var viewmodel = Viewmodel()
    private var token = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_repo_choose)
        binding.repoCompareViewModel = viewmodel

        setSupportActionBar(binding.toolbar) //커스텀한 toolbar를 액션바로 사용
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.back)

        token = intent.getStringExtra("token")!!

        binding.repoCompare1.setOnClickListener {
            val intent = Intent(applicationContext, CompareSearchActivity::class.java)
            intent.putExtra("token", token)
            intent.putExtra("count", 0)
            activityResultLauncher.launch(intent)
        }

        binding.repoCompare2.setOnClickListener {
            val intent = Intent(applicationContext, CompareSearchActivity::class.java)
            intent.putExtra("token", token)
            intent.putExtra("count", 1)
            activityResultLauncher.launch(intent)
        }

        binding.repoChoose.setOnClickListener {
            if(binding.repoCompare1.text.isNullOrBlank() || binding.repoCompare2.text.isNullOrBlank() ||
                binding.repoCompare1.text == "Repository 1" || binding.repoCompare2.text == "Repository 2") {
                Toast.makeText(applicationContext, "비교할 Repository를 선택해 주세요!!", Toast.LENGTH_SHORT).show()
            } else {
                if(binding.repoCompare1.text.toString() != binding.repoCompare2.text.toString()) {
                    val intent = Intent(applicationContext, RepoCompareActivity::class.java)
                    intent.putExtra("repo1", binding.repoCompare1.text.toString())
                    intent.putExtra("repo2", binding.repoCompare2.text.toString())
                    intent.putExtra("token", token)
                    startActivity(intent)
                } else {
                    Toast.makeText(applicationContext,"서로 다른 Repository를 선택해 주세요!!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}