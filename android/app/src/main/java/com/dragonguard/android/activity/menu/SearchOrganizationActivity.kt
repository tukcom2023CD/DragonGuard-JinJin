package com.dragonguard.android.activity.menu

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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dragonguard.android.R
import com.dragonguard.android.activity.basic.MainActivity
import com.dragonguard.android.databinding.ActivitySearchOrganizationBinding
import com.dragonguard.android.model.org.OrganizationNamesModel
import com.dragonguard.android.adapters.SearchOrganizationAdapter
import com.dragonguard.android.viewmodel.Viewmodel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class SearchOrganizationActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchOrganizationBinding
    lateinit var searchOrgAdapter: SearchOrganizationAdapter
    private var token = ""
    private var viewmodel = Viewmodel()
    private var position = 0
    private var count = 0
    private var type = ""
    private var lastSearch = ""
    private var changable = true
    private var typeChanged = false
    private var orgNames = OrganizationNamesModel()
    private var chosenType: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search_organization)
        binding.searchOrganizationBinding = viewmodel

        token = intent.getStringExtra("token")!!
        chosenType = intent?.getStringExtra("type")
        if(chosenType != null) {
            when (chosenType) {
                "대학교" -> {
                    binding.orgGroup.check(R.id.org_type0)
                    type = "UNIVERSITY"
                }
                "고등학교" -> {
                    binding.orgGroup.check(R.id.org_type1)
                    type = "HIGH_SCHOOL"
                }
                "회사" -> {
                    binding.orgGroup.check(R.id.org_type2)
                    type = "COMPANY"
                }
                "etc" -> {
                    binding.orgGroup.check(R.id.org_type3)
                    type = "ETC"
                }

            }
            binding.orgGroup.isEnabled = false
        }

        setSupportActionBar(binding.toolbar) //커스텀한 toolbar를 액션바로 사용
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.back)
        supportActionBar?.title = "   조직 검색 및 인증"

        binding.orgGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.org_type0 -> {
                    type = "UNIVERSITY"
                }
                R.id.org_type0 -> {
                    type = "HIGH_SCHOOL"
                }
                R.id.org_type2 -> {
                    type = "COMPANY"
                }
                R.id.org_type3 -> {
                    type = "ETC"
                }

            }
            typeChanged = true
        }
        //        검색 아이콘 눌렀을때 검색 구현
        binding.searchIcon.setOnClickListener {
            if (!viewmodel.onSearchListener.value.isNullOrEmpty()) {
                if (lastSearch != viewmodel.onSearchListener.value!! || typeChanged) {
                    orgNames.clear()
                    binding.searchResult.visibility = View.GONE
                    count = 0
                    position = 0
                    typeChanged = false
                }
                changable = true
                lastSearch = viewmodel.onSearchListener.value!!
                getOrganizationNames(viewmodel.onSearchListener.value!!)
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
                        if (lastSearch != viewmodel.onSearchListener.value!! || typeChanged) {
                            orgNames.clear()
                            binding.searchResult.visibility = View.GONE
                            count = 0
                            position = 0
                            typeChanged = false
                        }
                        changable = true
                        lastSearch = viewmodel.onSearchListener.value!!
                        getOrganizationNames(viewmodel.onSearchListener.value!!)
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

        binding.noneOrganization.setOnClickListener {
            val intent = Intent(applicationContext, RegistOrgActivity::class.java)
            intent.putExtra("token", token)
            startActivity(intent)
        }
    }

    fun getOrganizationNames(name: String) {
        Log.d("org 검색", "이름 $name type $type  count $count")
        val coroutine = CoroutineScope(Dispatchers.Main)
        coroutine.launch {
            if(!this@SearchOrganizationActivity.isFinishing) {
                if(type.isBlank()) {
                    type = "UNIVERSITY"
                }
                val resultDeferred = coroutine.async(Dispatchers.IO) {
                    viewmodel.getOrgNames(name, token, type, count)
                }
                val result = resultDeferred.await()
                if(checkSearchResult(result)) {
                    initRecycler()
                } else {
                    val secondDeferred = coroutine.async(Dispatchers.IO) {
                        viewmodel.getOrgNames(name, token, type, count)
                    }
                    val second = secondDeferred.await()
                    if (checkSearchResult(second)) {
                        initRecycler()
                    } else {
                        binding.progressBar.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun checkSearchResult(searchResult: OrganizationNamesModel): Boolean {
        return when (searchResult.isNullOrEmpty()) {
            true -> {
                if (changable) {
                    changable = false
                    false
                } else {
                    true
                }
            }
            false -> {
                changable = false
                Log.d("api 시도", "api 성공$searchResult")
                for (i in 0 until searchResult.size) {
                    val compare = orgNames.filter { it.name == searchResult[i].name }
                    if (compare.isEmpty()) {
                        orgNames.add(searchResult[i])
                    }
                }
                true
            }
        }

    }

    private fun initRecycler() {
        Log.d("count", "count: $count")
        if (count == 0) {
            binding.orgListTitle.text = "$type 목록"
            searchOrgAdapter = SearchOrganizationAdapter(orgNames, this, token)
            binding.searchResult.adapter = searchOrgAdapter
            binding.searchResult.layoutManager = LinearLayoutManager(this)
            searchOrgAdapter.notifyDataSetChanged()
            binding.searchResult.visibility = View.VISIBLE
        }
        count++
        binding.searchResult.adapter?.notifyDataSetChanged()
        Log.d("api 횟수", "$count 페이지 검색")
        binding.progressBar.visibility = View.GONE
        initScrollListener()
    }

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

    private fun loadMorePosts() {
        if (binding.progressBar.visibility == View.GONE && count != 0) {
            binding.progressBar.visibility = View.VISIBLE
            changable = true
            CoroutineScope(Dispatchers.Main).launch {
                getOrganizationNames(lastSearch)
            }
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

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.home, binding.toolbar.menu)
//        return true
//    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
//            R.id.home_menu -> {
//                val intent = Intent(applicationContext, MainActivity::class.java)
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
//                startActivity(intent)
//            }
        }
        return super.onOptionsItemSelected(item)
    }
}