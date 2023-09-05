package com.dragonguard.android.activity.search

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import com.dragonguard.android.R
import com.dragonguard.android.databinding.ActivitySearchFilterBinding
import com.dragonguard.android.fragment.FilterSheetFragment

class SearchFilterActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchFilterBinding
    val map = HashMap<String, String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchFilterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val languages = mutableListOf("C", "C#", "C++", "CoffeeScript ", "CSS", "Dart", "DM", "Elixir", "Go", "Groovy", "HTML", "Java", "JavaScript",
            "Kotlin", "Objective-C", "Perl", "PHP", "PowerShell", "Python", "Ruby", "Rust", "Scala", "Shell", "Swift", "TypeScript")
        val extras = mutableListOf("10개 미만", "50개 미만", "100개 미만", "500개 미만", "500개 이상")
        val topics = mutableListOf("0개", "1개", "2개", "3개", "4개 이상")

        setSupportActionBar(binding.toolbar) //커스텀한 toolbar를 액션바로 사용
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.back)

        binding.filterUser.setOnClickListener {
            binding.languageFilters.removeAllViews()
            binding.starFilters.removeAllViews()
            binding.topicFilters.removeAllViews()
            binding.forkFilters.removeAllViews()
            binding.filterContent.visibility = View.INVISIBLE
            map.clear()
            map.put("type", "USERS")
        }

        binding.filterRepository.setOnClickListener {
            map.put("type", "REPOSITORIES")
            binding.filterContent.visibility = View.VISIBLE
        }

        binding.languageFilter.setOnClickListener {
            val bottomSheetFragment = FilterSheetFragment(this, languages, "language", binding)
            bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
        }

        binding.starFilter.setOnClickListener {
            val bottomSheetFragment = FilterSheetFragment(this, extras, "stars", binding)
            bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
        }

        binding.forkFilter.setOnClickListener {
            val bottomSheetFragment = FilterSheetFragment(this, extras, "forks", binding)
            bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
        }

        binding.topicFilter.setOnClickListener {
            val bottomSheetFragment = FilterSheetFragment(this, topics, "topics", binding)
            bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
        }

        binding.searchName.setOnEditorActionListener { textView, i, keyEvent ->
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                map.put("name", binding.searchName.text.toString())
                val intent = Intent(applicationContext, SearchActivity::class.java)
                intent.putExtra("type", map["type"])
                intent.putExtra("language", map["language"])
                intent.putExtra("stars", map["stars"])
                intent.putExtra("forks", map["forks"])
                intent.putExtra("topics", map["topics"])
                intent.putExtra("name", map["name"])
                setResult(0, intent)
                finish()
            }
            true
        }

    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        closeKeyboard()
        return super.dispatchTouchEvent(ev)
    }

    //    edittext의 키보드 제거
    fun closeKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.searchName.windowToken, 0)
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