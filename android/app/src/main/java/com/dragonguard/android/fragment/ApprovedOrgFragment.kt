package com.dragonguard.android.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dragonguard.android.R
import com.dragonguard.android.databinding.FragmentApprovedOrgBinding
import com.dragonguard.android.enums.RequestStatus
import com.dragonguard.android.model.org.ApproveRequestOrgModelItem
import com.dragonguard.android.adapters.ApprovedOrgAdapter
import com.dragonguard.android.viewmodel.Viewmodel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ApprovedOrgFragment(private val token: String) : Fragment() {
    private lateinit var binding: FragmentApprovedOrgBinding
    private var viewmodel = Viewmodel()
    private var count = 0
    private var page = 0
    private var orgList = ArrayList<ApproveRequestOrgModelItem>()
    private var position = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_approved_org, container, false)
        binding.approvedOrgViewmodel = viewmodel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onResume() {
        super.onResume()
        requestList()
    }

    private fun requestList() {
        val coroutine = CoroutineScope(Dispatchers.Main)
        Log.d("요청", "승인된 요청")
        coroutine.launch {
            if (!this@ApprovedOrgFragment.isRemoving && count < 3) {
                val resultDeferred = coroutine.async(Dispatchers.IO) {
                    viewmodel.statusOrgList(RequestStatus.ACCEPTED.status, page, token)
                }
                val result = resultDeferred.await()
                if(!result.isEmpty()) {
                    result.forEach {
                        if(!orgList.contains(it)) {
                            orgList.add(it)
                        }
                    }
                    initRecycler()
                } else {
                    count++
                    requestList()
                }
            }
        }
    }

    private fun initRecycler() {
        Log.d("count", "count: $count")
        if (page == 0) {
            val adapter = ApprovedOrgAdapter(orgList, requireContext(), token)
            binding.acceptedOrgList.adapter = adapter
            binding.acceptedOrgList.layoutManager = LinearLayoutManager(requireContext())
            adapter.notifyDataSetChanged()
            binding.acceptedOrgList.visibility = View.VISIBLE
        }
        Log.d("list", "결과 : $orgList")
        page++
        binding.acceptedOrgList.adapter?.notifyDataSetChanged()
        initScrollListener()
    }
    private fun loadMorePosts() {
        if (page != 0) {
            CoroutineScope(Dispatchers.Main).launch {
                requestList()
            }
        }
    }

    //마지막 item에서 스크롤 하면 로딩과 함께 다시 받아서 추가하기
    private fun initScrollListener() {
        binding.acceptedOrgList.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = binding.acceptedOrgList.layoutManager
                val lastVisibleItem = (layoutManager as LinearLayoutManager)
                    .findLastCompletelyVisibleItemPosition()
                val itemTotalCount = recyclerView.adapter!!.itemCount - 1
                position = recyclerView.adapter!!.itemCount - 1
                // 마지막으로 보여진 아이템 position 이
                // 전체 아이템 개수보다 1개 모자란 경우, 데이터를 loadMore 한다
                if (!binding.acceptedOrgList.canScrollVertically(1) && lastVisibleItem == itemTotalCount) {
                    loadMorePosts()
                }
            }
        })
    }
}