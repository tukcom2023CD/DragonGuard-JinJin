package com.dragonguard.android.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.dragonguard.android.activity.search.SearchFilterActivity
import com.dragonguard.android.adapters.UserListAdapter
import com.dragonguard.android.databinding.FilterSheetBinding
import com.dragonguard.android.databinding.FragmentCompareUserBinding
import com.dragonguard.android.databinding.UserSheetBinding
import com.dragonguard.android.model.contributors.GitRepoMember
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class UserSheetfragment (private val context: CompareUserFragment, private val firstList: ArrayList<GitRepoMember>,
                         private val second: ArrayList<GitRepoMember>, private val type: Int, private val repo1: String, private val repo2: String,
                         private val fragmentBinding: FragmentCompareUserBinding
) : BottomSheetDialogFragment() {
    private lateinit var binding: UserSheetBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = UserSheetBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.filterTitleFirst.text = repo1
        binding.filterTitleSecond.text = repo2
        binding.firstFilterItems.adapter = UserListAdapter(firstList, context, type, fragmentBinding)
        binding.firstFilterItems.layoutManager = LinearLayoutManager(requireContext())
        binding.firstFilterItems.adapter?.notifyDataSetChanged()

        binding.secondFilterItems.adapter = UserListAdapter(second, context, type, fragmentBinding)
        binding.secondFilterItems.layoutManager = LinearLayoutManager(requireContext())
        binding.secondFilterItems.adapter?.notifyDataSetChanged()
    }

}