package com.dragonguard.android.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dragonguard.android.databinding.ApproveRequestListBinding
import com.dragonguard.android.enums.RequestStatus
import com.dragonguard.android.fragment.ApproveOrgFragment
import com.dragonguard.android.model.org.ApproveRequestOrgModelItem
import com.dragonguard.android.viewmodel.Viewmodel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

//승인 요청중인 조직 목록 adapter
class ApproveRequestOrgAdapter (private var datas : ArrayList<ApproveRequestOrgModelItem>, private val context: Context,
                                private val token: String, private val viewmodel: Viewmodel, private val frag: ApproveOrgFragment
) : RecyclerView.Adapter<ApproveRequestOrgAdapter.ViewHolder>() {
    private var count = 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ApproveRequestListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
    override fun getItemCount(): Int = datas.size

    //리사이클러 뷰의 요소들을 넣어줌
    inner class ViewHolder(private val binding: ApproveRequestListBinding) : RecyclerView.ViewHolder(binding.root) {
        private var current = 0
        fun bind(data1: ApproveRequestOrgModelItem, currentPosition: Int) {
            current = currentPosition
            binding.requestOrgName.text = data1.name
            binding.requestOrgType.text = data1.type
            binding.emailEndpoint.text = data1.email_endpoint
            binding.approveOrgBtn.setOnClickListener {
                approveApproval(data1, RequestStatus.ACCEPTED, current)
                notifyDataSetChanged()
            }
            binding.rejectOrgBtn.setOnClickListener {
                rejectApproval(data1, RequestStatus.DENIED, current)
                notifyDataSetChanged()
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datas[position], position)
    }
    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }
    override fun getItemViewType(position: Int): Int {
        return position
    }

    private fun approveApproval(data1: ApproveRequestOrgModelItem, status: RequestStatus, currentPosition: Int) {
        val coroutine = CoroutineScope(Dispatchers.Main)
        coroutine.launch {
            if (count < 5) {
                val resultDeferred = coroutine.async(Dispatchers.IO) {
                    viewmodel.approveOrgRequest(data1.id, status.status, token)
                }
                val result = resultDeferred.await()
                datas.removeAt(currentPosition)
                notifyItemRemoved(currentPosition)
                viewmodel.onApproveOrgListener.value = true
                count = 0
            }
        }
    }
    private fun rejectApproval(data1: ApproveRequestOrgModelItem, status: RequestStatus, currentPosition: Int) {
        val coroutine = CoroutineScope(Dispatchers.Main)
        coroutine.launch {
            if (count < 5) {
                val resultDeferred = coroutine.async(Dispatchers.IO) {
                    viewmodel.approveOrgRequest(data1.id, status.status, token)
                }
                val result = resultDeferred.await()
                datas.removeAt(currentPosition)
                notifyItemRemoved(currentPosition)
                viewmodel.onRejectOrgListener.value = true
                count = 0
            }
        }
    }

}