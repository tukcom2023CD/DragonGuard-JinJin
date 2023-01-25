package com.dragonguard.android.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dragonguard.android.R
import com.dragonguard.android.databinding.ActivitySearchBinding
import com.dragonguard.android.recycleradapter.HorizontalItemDecorator
import com.dragonguard.android.recycleradapter.RepositoryProfileAdapter
import com.dragonguard.android.recycleradapter.VerticalItemDecorator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.internal.wait
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding
    lateinit var repositoryProfileAdapter : RepositoryProfileAdapter
    private var position = 0
    private var array1= ArrayList<String>()
    private val backendIp = ""
    private var count = 0
    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(1, TimeUnit.MINUTES)
        .readTimeout(50, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search)
        binding.searchActivity = this

        val array2 = arrayListOf("DragonGuard-JinJin", "MBTI", "RISING","BOSS","AOK","GoldenClinic", "Pass", "BTS", "EVE")
        initRecycler(array2)

        binding.searchName.setOnKeyListener { view, i, keyEvent ->
            if(keyEvent.action == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_ENTER){
                val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding.searchName.windowToken, 0)
                true
            }else{
                false
            }
        }
    }

    fun onOptionListener(){
        if(binding.optionIcon.tag == "down"){
            binding.optionIcon.setImageResource(R.drawable.ic_baseline_arrow_drop_up_24)
            binding.optionIcon.tag = "up"
        }else{
            binding.optionIcon.setImageResource(R.drawable.ic_baseline_arrow_drop_down_24)
            binding.optionIcon.tag = "down"
        }
    }

    fun onSearchListener(){
        Toast.makeText(applicationContext, "${binding.searchName.text} 검색", Toast.LENGTH_SHORT).show()
    }

    fun initRecycler(array: ArrayList<String>){
        array1.addAll(array)
        if(count == 0){
            repositoryProfileAdapter = RepositoryProfileAdapter(array1, this)
            binding.searchResult.addItemDecoration(VerticalItemDecorator(20))
            binding.searchResult.addItemDecoration(HorizontalItemDecorator(10))
            binding.searchResult.adapter = repositoryProfileAdapter
            binding.searchResult.layoutManager = LinearLayoutManager(this)
            repositoryProfileAdapter.notifyDataSetChanged()
            binding.searchResult.visibility = View.VISIBLE
//            Toast.makeText(applicationContext, "첫 추가 완료!! $count", Toast.LENGTH_SHORT).show()
            count++
        }
        else{
            binding.searchResult.scrollToPosition(position)
//            Toast.makeText(applicationContext, "추가 완료!! $count", Toast.LENGTH_SHORT).show()
        }
        binding.loading.visibility = View.GONE
        initScrollListener()
    }

    private fun loadMorePosts() {
        if (binding.loading.visibility == View.GONE) {
            binding.loading.visibility = View.VISIBLE
            CoroutineScope(Dispatchers.Main).launch {
/*              데이터를 추가로 받아오는 부분 구현

                val retrofitD =
                    Retrofit.Builder().baseUrl(backendIp)
                        .client(okHttpClient)
                        .addConverterFactory(GsonConverterFactory.create()).build()
                val apiD = retrofitD.create(DockerJejuPlaceApi::class.java)

                apiCallD(apiD, label, split)
 */
                val array = arrayListOf("name","string")
                initRecycler(array)
            }
        }
    }

    //마지막 item에서 스크롤 하면 로딩과 함께 다시 받아서 추가하기
    private fun initScrollListener() {
        binding.searchResult.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = binding.searchResult.layoutManager
                // hasNextPage() -> 다음 페이지가 있는 경우
//                if (resultDecD <4401 ) {
                    val lastVisibleItem = (layoutManager as LinearLayoutManager)
                        .findLastCompletelyVisibleItemPosition()
                    val itemTotalCount = recyclerView.adapter!!.itemCount - 1
                    position = recyclerView.adapter!!.itemCount - 1
                    // 마지막으로 보여진 아이템 position 이
                    // 전체 아이템 개수보다 5개 모자란 경우, 데이터를 loadMore 한다
                    if (!binding.searchResult.canScrollVertically(1) && lastVisibleItem == itemTotalCount) {
//                        Toast.makeText(applicationContext, "끝에 도달!!", Toast.LENGTH_SHORT).show()
                        loadMorePosts()
                    }
//                }
            }
        })
    }
}