package com.dragonguard.android.activity.compare

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dragonguard.android.R
import com.dragonguard.android.activity.basic.MainActivity
import com.dragonguard.android.databinding.ActivitySearchBinding
import com.dragonguard.android.model.search.RepoSearchResultModel
import com.dragonguard.android.adapters.SearchCompareRepoAdapter
import com.dragonguard.android.viewmodel.Viewmodel
import kotlinx.coroutines.*

class CompareSearchActivity : AppCompatActivity() {
    private lateinit var compareRepositoryAdapter: SearchCompareRepoAdapter
    private lateinit var binding : ActivitySearchBinding
    var viewmodel = Viewmodel()
    private var position = 0
    private var repoNames = ArrayList<RepoSearchResultModel>()
    private var count = 0
    private var changed = true
    private var lastSearch = ""
    private var repoCount = 0
    private var token = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search)
        binding.searchViewModel = viewmodel

        setSupportActionBar(binding.toolbar) //커스텀한 toolbar를 액션바로 사용
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.back)

        token = intent.getStringExtra("token")!!
        repoCount = intent.getIntExtra("count", 0)
        if(repoCount == 0) {
            supportActionBar?.setTitle("첫번째 Repository")
        } else {
            supportActionBar?.setTitle("두번째 Repository")
        }
//        Toast.makeText(this, "$repoCount", Toast.LENGTH_SHORT).show()

        viewmodel.onOptionListener.observe(this, Observer {

/*            화면전환 테스트
            val intent = Intent(applicationContext, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
 */

        })


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
                        if (lastSearch != viewmodel.onSearchListener.value!!) {
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
                            "엔터 검색어를 입력하세요!!",
                            Toast.LENGTH_SHORT
                        ).show()
                        closeKeyboard()
                    }

                }
            }
            true
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
        val coroutine = CoroutineScope(Dispatchers.Main)
        coroutine.launch {
            if(!this@CompareSearchActivity.isFinishing) {
                val resultDeferred = coroutine.async(Dispatchers.IO) {
                    viewmodel.getSearchRepoResult(name, count, "REPOSITORIES", token)
                }
                val result = resultDeferred.await()
                delay(1000)
                if (!checkSearchResult(result)) {
                    val secondDeferred = coroutine.async(Dispatchers.IO) {
                        viewmodel.getSearchRepoResult(name, count,"REPOSITORIES", token)
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
            compareRepositoryAdapter = SearchCompareRepoAdapter(repoNames, this, repoCount, token)
            binding.searchResult.adapter = compareRepositoryAdapter
            binding.searchResult.layoutManager = LinearLayoutManager(this)
            compareRepositoryAdapter.notifyDataSetChanged()
            binding.searchResult.visibility = View.VISIBLE
        }
        count++
        binding.searchResult.adapter?.notifyDataSetChanged()
        Log.d("api 횟수", "$count 페이지 검색")
        binding.loadingLottie.visibility = View.GONE
        initScrollListener()
    }


    //    데이터 더 받아오는 함수 loadMorePosts() 구현
    private fun loadMorePosts() {
        if (binding.loadingLottie.visibility == View.GONE && count != 0) {
            binding.loadingLottie.visibility = View.VISIBLE
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