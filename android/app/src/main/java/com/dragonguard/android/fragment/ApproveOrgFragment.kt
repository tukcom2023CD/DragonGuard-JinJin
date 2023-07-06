package com.dragonguard.android.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dragonguard.android.R
import com.dragonguard.android.databinding.FragmentApproveOrgBinding
import com.dragonguard.android.enums.RequestStatus
import com.dragonguard.android.model.org.ApproveRequestOrgModelItem
import com.dragonguard.android.adapters.ApproveRequestOrgAdapter
import com.dragonguard.android.viewmodel.Viewmodel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class ApproveOrgFragment(private val token: String) : Fragment() {
    private lateinit var binding: FragmentApproveOrgBinding
    private var viewmodel = Viewmodel()
    var page = 0
    private var count = 0
    private var orgList = ArrayList<ApproveRequestOrgModelItem>()
    private var position = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_approve_org, container, false)
        binding.approveOrgBinding = viewmodel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewmodel.onApproveOrgListener.observe(requireActivity(), Observer {
            if(viewmodel.onApproveOrgListener.value == true) {
                Toast.makeText(requireContext(), "승인됨", Toast.LENGTH_SHORT).show()
                binding.waitingOrgList.adapter?.notifyDataSetChanged()
                viewmodel.onApproveOrgListener.value = false
            }
        })

        viewmodel.onRejectOrgListener.observe(requireActivity(), Observer {
            if(viewmodel.onRejectOrgListener.value == true) {
                Toast.makeText(requireContext(), "반려됨", Toast.LENGTH_SHORT).show()
                binding.waitingOrgList.adapter?.notifyDataSetChanged()
                viewmodel.onRejectOrgListener.value = false
            }
        })
    }

    override fun onResume() {
        super.onResume()
        requestList()
    }

    private fun requestList() {
        val coroutine = CoroutineScope(Dispatchers.Main)
        coroutine.launch {
            if (!this@ApproveOrgFragment.isRemoving && count < 5) {
                val resultDeferred = coroutine.async(Dispatchers.IO) {
                    viewmodel.statusOrgList(RequestStatus.REQUESTED.status, page, token)
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
            val adapter = ApproveRequestOrgAdapter(orgList, requireContext(), token, viewmodel, this)
            binding.waitingOrgList.adapter = adapter
            binding.waitingOrgList.layoutManager = LinearLayoutManager(requireContext())
            adapter.notifyDataSetChanged()
            binding.waitingOrgList.visibility = View.VISIBLE
        }
        page++
        count = 0
        binding.waitingOrgList.adapter?.notifyDataSetChanged()
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
        binding.waitingOrgList.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = binding.waitingOrgList.layoutManager
                val lastVisibleItem = (layoutManager as LinearLayoutManager)
                    .findLastCompletelyVisibleItemPosition()
                val itemTotalCount = recyclerView.adapter!!.itemCount - 1
                position = recyclerView.adapter!!.itemCount - 1
                // 마지막으로 보여진 아이템 position 이
                // 전체 아이템 개수보다 1개 모자란 경우, 데이터를 loadMore 한다
                if (!binding.waitingOrgList.canScrollVertically(1) && lastVisibleItem == itemTotalCount) {
                    loadMorePosts()
                }
            }
        })
    }
}