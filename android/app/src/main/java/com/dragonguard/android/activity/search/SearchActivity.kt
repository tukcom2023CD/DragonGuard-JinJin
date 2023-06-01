package com.dragonguard.android.activity.search

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dragonguard.android.R
import com.dragonguard.android.activity.MainActivity
import com.dragonguard.android.model.search.RepoSearchResultModel
import com.dragonguard.android.databinding.ActivitySearchBinding
import com.dragonguard.android.recycleradapter.HorizontalItemDecorator
import com.dragonguard.android.recycleradapter.RepositoryProfileAdapter
import com.dragonguard.android.recycleradapter.VerticalItemDecorator
import com.dragonguard.android.viewmodel.Viewmodel
import kotlinx.coroutines.*

/*
 repo를 이름으로 검색하는 activity
 */
class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding
    lateinit var repositoryProfileAdapter: RepositoryProfileAdapter
    private var position = 0
    private var repoNames = ArrayList<RepoSearchResultModel>()
    private var count = 0
    private var changed = true
    private var lastSearch = ""
    private var popularLanguages = ArrayList<String>()
    private lateinit var languagesCheckBox: ArrayList<Boolean>
    var viewmodel = Viewmodel()
    private var filterLanguage = StringBuilder()
    private var filterOptions = StringBuilder()
    private var filterResult = StringBuilder()
    private var type = ""
    private var token = ""
    private val imgList = HashMap<String, Int>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search)
        binding.searchViewModel = viewmodel


        binding.searchResult.addItemDecoration(VerticalItemDecorator(20))
        binding.searchResult.addItemDecoration(HorizontalItemDecorator(10))

        setSupportActionBar(binding.toolbar) //커스텀한 toolbar를 액션바로 사용
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)

        val intent = intent
        token = intent.getStringExtra("token")!!
        popularLanguages = arrayListOf("C", "C#", "C++", "CoffeeScript ", "CSS", "Dart", "DM", "Elixir", "Go", "Groovy", "HTML", "Java", "JavaScript",
            "Kotlin", "Objective-C", "Perl", "PHP", "PowerShell", "Python", "Ruby", "Rust", "Scala", "Shell", "Swift", "TypeScript")
        imgList.apply {
            put("C", R.drawable.c)
            put("C#", R.drawable.c_sharp)
            put("C++", R.drawable.c_puls_plus)
            put("CoffeeScript", R.drawable.coffeescript)
            put("CSS", R.drawable.css)
            put("Dart", R.drawable.dart)
            put("Elixir", R.drawable.elixir)
            put("Go", R.drawable.go)
            put("Groovy", R.drawable.groovy)
            put("HTML", R.drawable.html_5)
            put("Java", R.drawable.java)
            put("JavaScript", R.drawable.javascript)
            put("Kotlin", R.drawable.kotlin)
            put("Objective-C", R.drawable.objective_c)
            put("Perl", R.drawable.perl)
            put("PHP", R.drawable.php)
            put("PowerShell", R.drawable.powershell)
            put("Python", R.drawable.python)
            put("Ruby", R.drawable.ruby)
            put("Rust", R.drawable.rust)
            put("Scala", R.drawable.scala)
            put("Shell", R.drawable.shell)
            put("Swift", R.drawable.swift)
            put("TypeScript", R.drawable.typescript)
        }
        languagesCheckBox = ArrayList<Boolean>()
        for(i in 0 until popularLanguages.size) {
            languagesCheckBox.add(false)
        }
//        Toast.makeText(applicationContext, "크기 : ${popularLanguages.size}", Toast.LENGTH_SHORT).show()
//        Toast.makeText(applicationContext, "checkbox : ${languagesCheckBox.size}", Toast.LENGTH_SHORT).show()
//        검색 옵션 구현



//        edittext에 엔터를 눌렀을때 검색되게 하는 리스너
        binding.searchName.setOnClickListener {
            val intent = Intent(this, SearchFilterActivity::class.java)
            activityResultLauncher.launch(intent)
        }
    }

    private val activityResultLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == 0) {
                val filterIntent = it.data
                try {
                    val language = filterIntent?.getStringExtra("language")
                    val type = filterIntent?.getStringExtra("type")
                    val topics = filterIntent?.getStringExtra("topics")
                    val stars = filterIntent?.getStringExtra("stars")
                    val forks = filterIntent?.getStringExtra("forks")
                    val name = filterIntent?.getStringExtra("name")
                    Log.d("results", "type: $type ")
                    Log.d("results", "language: $language ")
                    Log.d("results", "topics: $topics ")
                    Log.d("results", "stars: $stars ")
                    Log.d("results", "forks: $forks ")
                    Log.d("results", "name: $name ")
                    checkLanguage(language, type, topics, stars, forks, name)
                } catch (e: Exception) {

                }

            } else if (it.resultCode == 1) {

            }
        }


    private fun checkLanguage(languages: String?, type: String?, topics: String?, stars: String?, forks: String?, name: String?) {
        count = 0
        binding.searchOption.removeAllViews()
        filterResult = StringBuilder()
        filterLanguage = StringBuilder()
        filterOptions = StringBuilder()
        if(type != null) {
            this.type = type
        }
        val checked = arrayListOf<String>()
        val notCatLanguage = arrayListOf<String>()
        if(languages != null) {
            val splitLang = languages.split(",")
            splitLang.forEach(){
                if(it.isNotBlank()) {
                    checked.add("language:$it,")
                }
            }
        }

//        Log.d("filters", "filters: ${filterMap["stars"]}")
//        Toast.makeText(applicationContext, "index : $checked", Toast.LENGTH_SHORT).show()
        checked.forEachIndexed { index, s ->
            if(index != checked.size - 1) {
                filterLanguage.append("${checked[index]},")
                notCatLanguage.add(checked[index].substring(9,checked[index].lastIndex+1)+",")
            } else {
                filterLanguage.append(checked[index])
                notCatLanguage.add(checked[index].substring(9,checked[index].lastIndex+1)+"")
            }
        }
        if(topics != null) {
            when(topics) {
                "0개" -> {
                    filterOptions.append("topics:0,")
                }
                "1개" -> {
                    filterOptions.append("topics:0,")
                }
                "2개" -> {
                    filterOptions.append("topics:0,")
                }
                "3개" -> {
                    filterOptions.append("topics:0,")
                }
                "4개 이상" -> {
                    filterOptions.append("topics:0,")
                }
            }
        }
        if(stars != null) {
            when(stars) {
                "10개 미만" -> {
                    filterOptions.append("stars:$stars,")
                }
                "50개 미만" -> {
                    filterOptions.append("stars:$stars,")
                }
                "100개 미만" -> {
                    filterOptions.append("stars:$stars,")
                }
                "500개 미만" -> {
                    filterOptions.append("stars:$stars,")
                }
                "500개 이상" -> {
                    filterOptions.append("stars:$stars,")
                }
            }

        }
        if(forks != null) {
            when(forks) {
                "10개 미만" -> {
                    filterOptions.append("forks:$forks,")
                }
                "50개 미만" -> {
                    filterOptions.append("forks:$forks,")
                }
                "100개 미만" -> {
                    filterOptions.append("forks:$forks,")
                }
                "500개 미만" -> {
                    filterOptions.append("forks:$forks,")
                }
                "500개 이상" -> {
                    filterOptions.append("forks:$forks,")
                }
            }
        }
//        count = 0
//        Toast.makeText(applicationContext, "filters:$filterOptions", Toast.LENGTH_SHORT).show()
//        Toast.makeText(applicationContext, "filters:$filterLanguage", Toast.LENGTH_SHORT).show()
        filterCat(name!!)
    }

    private fun filterCat(name: String) {
        if(filterLanguage.isNotEmpty()) {
            if (filterOptions.isNotEmpty()) {
                filterResult.append(filterLanguage)
                filterResult.append(",")
                filterResult.append(filterOptions)
                filterResult.deleteAt(filterResult.lastIndex)
            } else {
                filterResult.append(filterLanguage)
            }
        } else {
            if (filterOptions.isNotEmpty()) {
                filterResult.append(filterOptions)
                filterResult.deleteAt(filterResult.lastIndex)
            } else {
                filterResult = StringBuilder()
            }
        }

        callSearchApi(name)


        binding.searchOption.visibility = View.VISIBLE
//        binding.chosenFilters.text = chosenFilters.toString()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home, binding.toolbar.menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
            R.id.home_menu -> {
                val intent = Intent(applicationContext, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    //    화면의 다른곳 눌렀을때 처리
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        closeKeyboard()
        return super.dispatchTouchEvent(ev)
    }

    //    edittext의 키보드 제거
    fun closeKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.searchName.windowToken, 0)
    }

    //    repo 검색 api 호출 및 결과 출력
    private fun callSearchApi(name: String) {
        binding.loadingLottie.visibility = View.VISIBLE
        binding.loadingLottie.playAnimation()
        Log.d("필터", filterResult.toString())
        val coroutine = CoroutineScope(Dispatchers.Main)
        coroutine.launch {
            if(!this@SearchActivity.isFinishing) {
                if(type.isNotBlank()) {
                    if(filterResult.toString().isNotEmpty()) {
                        val resultDeferred = coroutine.async(Dispatchers.IO) {
                            viewmodel.getRepositoryNamesWithFilters(name, count, filterResult.toString(), type, token)
                        }
                        val result = resultDeferred.await()
                        delay(1000)
                        if (!checkSearchResult(result)) {
                            val secondDeferred = coroutine.async(Dispatchers.IO) {
                                viewmodel.getRepositoryNamesWithFilters(name, count, filterResult.toString(), type, token)
                            }
                            val second = secondDeferred.await()
                            if (checkSearchResult(second)) {
                                initRecycler()
                            } else {
                                binding.loadingLottie.visibility = View.GONE
                            }
                        } else {
                            initRecycler()
                        }
                    } else {
                        val resultDeferred = coroutine.async(Dispatchers.IO) {
                            viewmodel.getSearchRepoResult(name, count, type, token)
                        }
                        val result = resultDeferred.await()
                        delay(1000)
                        if (!checkSearchResult(result)) {
                            val secondDeferred = coroutine.async(Dispatchers.IO) {
                                viewmodel.getSearchRepoResult(name, count, type, token)
                            }
                            val second = secondDeferred.await()
                            if (checkSearchResult(second)) {
                                initRecycler()
                            } else {
                                binding.loadingLottie.visibility = View.GONE
                            }
                        } else {
                            initRecycler()
                        }
                    }
                } else {
                    val resultDeferred = coroutine.async(Dispatchers.IO) {
                        viewmodel.getSearchRepoResult(name, count, "REPOSITORIES", token)
                    }
                    val result = resultDeferred.await()
                    delay(1000)
                    if (!checkSearchResult(result)) {
                        val secondDeferred = coroutine.async(Dispatchers.IO) {
                            viewmodel.getSearchRepoResult(name, count, "REPOSITORIES", token)
                        }
                        val second = secondDeferred.await()
                        if (checkSearchResult(second)) {
                            initRecycler()
                        } else {
                            binding.loadingLottie.visibility = View.GONE
                        }
                    } else {
                        initRecycler()
                    }
                }
            }
        }
    }

    //    api 호출결과 판별 및 출력
    private fun checkSearchResult(searchResult: ArrayList<RepoSearchResultModel>): Boolean {
        return when (searchResult.isNullOrEmpty()) {
            true -> {
                if (changed) {
                    changed = false
                    false
                } else {
                    true
                }
            }
            false -> {
                changed = false
                Log.d("api 시도", "api 성공$searchResult")
                for(i in 0 until searchResult.size) {
                    val compare = repoNames.filter { it.name == searchResult[i].name }
                    if(compare.isEmpty()) {
                        repoNames.add(searchResult[i])
                    }
                }
                true
            }
        }

    }


    //    받아온 데이터를 리사이클러뷰에 추가하는 함수 initRecycler()
    private fun initRecycler() {
        Log.d("count", "count: $count")
        if (count == 0) {
            Log.d("values", "names: $repoNames")
            repositoryProfileAdapter = if(type.isBlank()) {
                RepositoryProfileAdapter(repoNames, this, token, "REPOSITORIES", imgList)
            } else {
                RepositoryProfileAdapter(repoNames, this, token, type, imgList)
            }
            binding.searchResult.adapter = repositoryProfileAdapter
            binding.searchResult.layoutManager = LinearLayoutManager(this)
            repositoryProfileAdapter.notifyDataSetChanged()
            binding.searchResult.visibility = View.VISIBLE
        }
        count++
        Log.d("api 횟수", "$count 페이지 검색")
        binding.loadingLottie.pauseAnimation()
        binding.loadingLottie.visibility = View.GONE
        initScrollListener()
    }


    //    데이터 더 받아오는 함수 loadMorePosts() 구현
    private fun loadMorePosts() {
        if (binding.loadingLottie.visibility == View.GONE && count != 0) {
            val params = binding.loadingLottie.layoutParams as CoordinatorLayout.LayoutParams
            params.gravity = Gravity.BOTTOM
            params.bottomMargin = 100
            binding.loadingLottie.layoutParams = params
            binding.loadingLottie.visibility = View.VISIBLE
            binding.loadingLottie.playAnimation()
            changed = true
            CoroutineScope(Dispatchers.Main).launch {
                Log.d("api 시도", "callSearchApi 실행  load more")
                callSearchApi(lastSearch)
            }
        }
    }

    //마지막 item에서 스크롤 하면 로딩과 함께 다시 받아서 추가하기
    private fun initScrollListener() {
        binding.searchResult.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = binding.searchResult.layoutManager
                val lastVisibleItem = (layoutManager as LinearLayoutManager)
                    .findLastCompletelyVisibleItemPosition()
                val itemTotalCount = recyclerView.adapter!!.itemCount - 1
                position = recyclerView.adapter!!.itemCount - 1
                // 마지막으로 보여진 아이템 position 이
                // 전체 아이템 개수보다 1개 모자란 경우, 데이터를 loadMore 한다
                if (!binding.searchResult.canScrollVertically(1) && lastVisibleItem == itemTotalCount) {
                    loadMorePosts()
                }
            }
        })
    }
}