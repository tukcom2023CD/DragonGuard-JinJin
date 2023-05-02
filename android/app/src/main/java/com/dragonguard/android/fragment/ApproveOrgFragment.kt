package com.dragonguard.android.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.dragonguard.android.R
import com.dragonguard.android.databinding.FragmentApproveOrgBinding
import com.dragonguard.android.viewmodel.Viewmodel


class ApproveOrgFragment : Fragment() {
    private lateinit var binding: FragmentApproveOrgBinding
    private var viewmodel = Viewmodel()
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
        requestList()
    }

    private fun requestList() {
        initRecycler()
    }

    private fun initRecycler() {

    }

}