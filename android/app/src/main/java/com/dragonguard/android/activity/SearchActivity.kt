package com.dragonguard.android.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dragonguard.android.R
import com.dragonguard.android.databinding.ActivitySearchBinding
import com.dragonguard.android.recycleradapter.HorizontalItemDecorator
import com.dragonguard.android.recycleradapter.RepositoryProfileAdapter
import com.dragonguard.android.recycleradapter.VerticalItemDecorator
import com.dragonguard.android.viewmodel.SearchViewModel
import okhttp3.OkHttpClient
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
    var viewmodel = SearchViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search)
        binding.searchViewModel = viewmodel
//        val handler = Handler()

        val array2 = arrayListOf("DragonGuard-JinJin", "MBTI", "RISING","BOSS","AOK","GoldenClinic", "Pass", "BTS", "EVE")
        initRecycler(array2)
//        handler.postDelayed({initRecycler(array2)},2000)

//        검색 옵션 구현
        viewmodel.onOptionListener.observe(this, Observer {

/*            화면전환 테스트
            val intent = Intent(applicationContext, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)

 */

            if(viewmodel.onOptionListener.value == "down"){
                binding.optionIcon.setImageResource(R.drawable.ic_baseline_arrow_drop_down_24)
                Log.d("option", "down")
            }else{
                binding.optionIcon.setImageResource(R.drawable.ic_baseline_arrow_drop_up_24)
                Log.d("option", "up")
            }
        })


//        검색 아이콘 눌렀을때 검색 구현
        viewmodel.onIconClickListener.observe(this, Observer {
            Log.d("toast", "toast")
            if(!viewmodel.onSearchListener.value.isNullOrEmpty()){
                Toast.makeText(applicationContext, "아이콘  ${viewmodel.onSearchListener.value} 검색", Toast.LENGTH_SHORT).show()
                closeKeyboard()
            }else{
                Toast.makeText(applicationContext, "아이콘 검색어를 입력하세요!!", Toast.LENGTH_SHORT).show()
                closeKeyboard()
            }

        })
//        edittext에 엔터를 눌렀을때 검색되게 하는 리스너
        viewmodel.onSearchListener.observe(this, Observer {
            if(!viewmodel.onSearchListener.value.isNullOrEmpty() &&  viewmodel.onSearchListener.value!!.last() == '\n'){
                val search = binding.searchName.text.substring(0 until binding.searchName.text.length-1)
                binding.searchName.setText(search)
                if(search.isNotEmpty()){
                    Toast.makeText(applicationContext, "엔터 ${viewmodel.onSearchListener.value} 검색", Toast.LENGTH_SHORT).show()
                    closeKeyboard()
                }else{
                    binding.searchName.setText("")
                    Toast.makeText(applicationContext, "엔터 검색어를 입력하세요!!", Toast.LENGTH_SHORT).show()
                    closeKeyboard()
                }
            }
        })

    }

//    화면의 다른곳 눌렀을때 처리
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        closeKeyboard()
        return super.dispatchTouchEvent(ev)
    }

//    edittext의 키보드 제거
    fun closeKeyboard(){
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.searchName.windowToken, 0)
    }




//    받아온 데이터를 리사이클러뷰에 추가하는 함수 initRecycler()
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
        initScrollListener()
        binding.loading.visibility = View.GONE
    }


//    데이터 더 받아오는 함수 loadMorePosts() 구현
    private fun loadMorePosts() {
        if (binding.loading.visibility == View.GONE) {
            binding.loading.visibility = View.VISIBLE
/*     CoroutineScope(Dispatchers.Main).launch {
             데이터를 추가로 받아오는 부분 구현

            val retrofitD =
                Retrofit.Builder().baseUrl(backendIp)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create()).build()
            val apiD = retrofitD.create(DockerJejuPlaceApi::class.java)

            apiCallD(apiD, label, split)x
        }
*/
            val array = arrayListOf("name","string")
            initRecycler(array)
//            val handler= Handler()
//            handler.postDelayed({initRecycler(array)}, 2000)
        }
    }

//마지막 item에서 스크롤 하면 로딩과 함께 다시 받아서 추가하기
    private fun initScrollListener() {
        binding.searchResult.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = binding.searchResult.layoutManager
                // hasNextPage() -> 다음 페이지가 있는 경우
//                       if (resultDecD <4401 ) {
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