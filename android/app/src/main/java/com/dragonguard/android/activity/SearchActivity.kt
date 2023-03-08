package com.dragonguard.android.activity

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dragonguard.android.R
import com.dragonguard.android.model.RepoSearchResultModel
import com.dragonguard.android.databinding.ActivitySearchBinding
import com.dragonguard.android.dialog.FilterDialog
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
    private lateinit var filterDialog : FilterDialog
    private var filterMap = mutableMapOf<String,String>()
    private var filterLanguage = StringBuilder()
    private var filterOptions = StringBuilder()
    private var filterResult = StringBuilder()
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
        popularLanguages = arrayListOf("C", "C#", "C++", "CoffeeScript ", "CSS", "Dart", "DM", "Elixir", "Go", "Groovy", "HTML", "Java", "JavaScript",
            "Kotlin", "Objective-C", "Perl", "PHP", "PowerShell", "Python", "Ruby", "Rust", "Scala", "Shell", "Swift", "TypeScript")
        languagesCheckBox = ArrayList<Boolean>()
        for(i in 0 until popularLanguages.size) {
            languagesCheckBox.add(false)
        }
//        Toast.makeText(applicationContext, "크기 : ${popularLanguages.size}", Toast.LENGTH_SHORT).show()
//        Toast.makeText(applicationContext, "checkbox : ${languagesCheckBox.size}", Toast.LENGTH_SHORT).show()
        filterDialog = FilterDialog(popularLanguages, languagesCheckBox, binding.optionIcon, filterMap)
//        검색 옵션 구현
        viewmodel.onOptionListener.observe(this, Observer {

/*            화면전환 테스트
            val intent = Intent(applicationContext, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
 */
            if (viewmodel.onOptionListener.value == "down") {
                binding.optionIcon.setImageResource(R.drawable.ic_baseline_arrow_drop_down_24)
                Log.d("option", "down")
                checkLanguage()
            } else {
                binding.optionIcon.setImageResource(R.drawable.ic_baseline_arrow_drop_up_24)
                Log.d("option", "up")
                filterLanguage = StringBuilder()
                filterOptions = StringBuilder()
                filterResult = StringBuilder()
                binding.searchOption.visibility = View.GONE
                filterDialog.show(supportFragmentManager, "filtering")
            }
        })

//        검색 아이콘 눌렀을때 검색 구현
        binding.searchIcon.setOnClickListener {
            if (!viewmodel.onSearchListener.value.isNullOrEmpty()) {
                if (lastSearch != viewmodel.onSearchListener.value!! || filterLanguage.isNotEmpty() || filterOptions.isNotEmpty()) {
                    repoNames.clear()
                    binding.searchResult.visibility = View.GONE
                    count = 0
                    position = 0
                }
                changed = true
                lastSearch = viewmodel.onSearchListener.value!!
                Log.d("api 시도", "callSearchApi 실행")
                callSearchApi(viewmodel.onSearchListener.value!!)
                binding.searchResult.visibility = View.VISIBLE
                binding.searchName.isFocusable = true
            } else {
                Toast.makeText(applicationContext, "검색어를 입력하세요!!", Toast.LENGTH_SHORT).show()
                closeKeyboard()
            }
        }

//        edittext에 엔터를 눌렀을때 검색되게 하는 리스너
        binding.searchName.setOnEditorActionListener { textView, i, keyEvent ->
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                if (!viewmodel.onSearchListener.value.isNullOrEmpty()) {
                    Log.d("enter click", "edittext 클릭함")
                    val search = binding.searchName.text!!
                    binding.searchName.setText(search)
                    binding.searchName.setSelection(binding.searchName.length())
                    if (search.isNotEmpty()) {
                        closeKeyboard()
                        if (lastSearch != viewmodel.onSearchListener.value!! || filterLanguage.isNotEmpty() || filterOptions.isNotEmpty()) {
                            repoNames.clear()
                            binding.searchResult.visibility = View.GONE
                            count = 0
                            position = 0
                        }
                        changed = true
                        lastSearch = viewmodel.onSearchListener.value!!
                        Log.d("api 시도", "callSearchApi 실행")
                        callSearchApi(viewmodel.onSearchListener.value!!)
                        binding.searchResult.visibility = View.VISIBLE
                        binding.searchName.isFocusable = true
                    } else {
                        binding.searchName.setText("")
                        Toast.makeText(
                            applicationContext,
                            "검색어를 입력하세요!!",
                            Toast.LENGTH_SHORT
                        ).show()
                        closeKeyboard()
                    }

                }
            }
            true
        }
    }

    private fun checkLanguage() {
        val checked = arrayListOf<String>()
        val notCatLanguage = arrayListOf<String>()
        languagesCheckBox.forEachIndexed { index, b ->
            if(languagesCheckBox[index]){
                checked.add("language:"+popularLanguages[index])
            }
        }
//        Log.d("filters", "filters: ${filterMap["stars"]}")
//        Toast.makeText(applicationContext, "index : $checked", Toast.LENGTH_SHORT).show()
        checked.forEachIndexed { index, s ->
            if(index != checked.size - 1) {
                filterLanguage.append("${checked[index]},")
                notCatLanguage.add(checked[index].substring(9,checked[index].lastIndex+1)+", ")
            } else {
                filterLanguage.append(checked[index])
                notCatLanguage.add(checked[index].substring(9,checked[index].lastIndex+1)+"")
            }
        }
        val notCatFilters = arrayListOf<String>()
        var count = 0
        filterMap.forEach {
            if(count < filterMap.size-1) {
                filterOptions.append("${it.key}:${it.value},")
                notCatFilters.add("${it.key} ${it.value}, ")
                count++
            } else {
                filterOptions.append("${it.key}:${it.value}")
                notCatFilters.add("${it.key} ${it.value} ")
            }
        }
//        count = 0
//        Toast.makeText(applicationContext, "filters:$filterOptions", Toast.LENGTH_SHORT).show()
//        Toast.makeText(applicationContext, "filters:$filterLanguage", Toast.LENGTH_SHORT).show()
        filterCat(notCatLanguage, notCatFilters)
    }

    private fun filterCat(language: ArrayList<String>, filters: ArrayList<String>) {
        val chosenFilters = StringBuilder()
        if(filterLanguage.isNotEmpty()) {
            if (filterOptions.isNotEmpty()) {
                filterResult.append(filterLanguage)
                filterResult.append(",")
                filterResult.append(filterOptions)
            } else {
                filterResult.append(filterLanguage)
            }
        } else {
            if (filterOptions.isNotEmpty()) {
                filterResult.append(filterOptions)
            } else {
                filterResult = StringBuilder()
            }
        }
        language.forEach {
            chosenFilters.append(it)
        }
        chosenFilters.append(", ")
        filters.forEach {
            chosenFilters.append(it)
        }
        val split = chosenFilters.split(",")
        for(i in split.indices) {
            val linear = LinearLayout(this)
            linear.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
            linear.setBackgroundResource(R.drawable.roundc)
            linear.id = 1000000+i
            val listButton = Button(this)
            listButton.text = split[i]
            val type = Typeface.createFromAsset(assets, "IBMPlexSansKR-SemiBold.ttf")
            listButton.setTextColor(Color.BLACK)
            listButton.setBackgroundColor(Color.rgb(204,204,204))
            listButton.textSize = 20f
            listButton.id = 100000+i
            listButton.typeface = type
            listButton.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT)
            val close = ImageButton(this)
            close.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT)
            close.setImageResource(R.drawable.ic_baseline_clear_24)
            close.setOnClickListener {
                val id = this.findViewById<Button>(100000+i)
                val lin = this.findViewById<LinearLayout>(1000000+i)
                val sub = id.text.split(" ")
                if(sub.size == 1) {
                    languagesCheckBox[popularLanguages.indexOf(sub[0])] = false
                } else {
                    filterMap.remove(sub[0])
                }
                lin.removeAllViews()
                checkLanguage()
            }

        }
        binding.searchOption.visibility = View.VISIBLE
        binding.chosenFilters.text = chosenFilters.toString()
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
        binding.progressBar.visibility = View.VISIBLE
        val coroutine = CoroutineScope(Dispatchers.Main)
        coroutine.launch {
            if(filterResult.toString().isNotEmpty()) {
                val resultDeferred = coroutine.async(Dispatchers.IO) {
                    viewmodel.getRepositoryNamesWithFilters(name, count, filterResult.toString())
                }
                val result = resultDeferred.await()
                delay(1000)
                if (!checkSearchResult(result)) {
                    val secondDeferred = coroutine.async(Dispatchers.IO) {
                        viewmodel.getRepositoryNamesWithFilters(name, count, filterResult.toString())
                    }
                    val second = secondDeferred.await()
                    if (checkSearchResult(second)) {
                        initRecycler()
                    } else {
                        binding.progressBar.visibility = View.GONE
                    }
                } else {
                    initRecycler()
                }
            } else {
                val resultDeferred = coroutine.async(Dispatchers.IO) {
                    viewmodel.getSearchRepoResult(name, count)
                }
                val result = resultDeferred.await()
                delay(1000)
                if (!checkSearchResult(result)) {
                    val secondDeferred = coroutine.async(Dispatchers.IO) {
                        viewmodel.getSearchRepoResult(name, count)
                    }
                    val second = secondDeferred.await()
                    if (checkSearchResult(second)) {
                        initRecycler()
                    } else {
                        binding.progressBar.visibility = View.GONE
                    }
                } else {
                    initRecycler()
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
            repositoryProfileAdapter = RepositoryProfileAdapter(repoNames, this)
            binding.searchResult.adapter = repositoryProfileAdapter
            binding.searchResult.layoutManager = LinearLayoutManager(this)
            repositoryProfileAdapter.notifyDataSetChanged()
            binding.searchResult.visibility = View.VISIBLE
        }
        count++
        Log.d("api 횟수", "$count 페이지 검색")
        binding.progressBar.visibility = View.GONE
        initScrollListener()
    }


    //    데이터 더 받아오는 함수 loadMorePosts() 구현
    private fun loadMorePosts() {
        if (binding.progressBar.visibility == View.GONE && count != 0) {
            binding.progressBar.visibility = View.VISIBLE
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