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
import com.dragonguard.android.model.contributors.GitRepoMember
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class UserSheetfragment (private val context: CompareUserFragment, private val list: ArrayList<GitRepoMember>, private val type: Int
                         , private val fragmentBinding: FragmentCompareUserBinding
) : BottomSheetDialogFragment() {
    private lateinit var binding: FilterSheetBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FilterSheetBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        when(type) {
            1 -> {
                binding.filterTitle.text = "User1"
            }
            2 -> {
                binding.filterTitle.text = "User2"
            }
        }
        binding.filterItems.adapter = UserListAdapter(list, context, type, fragmentBinding)
        binding.filterItems.layoutManager = LinearLayoutManager(requireContext())
        binding.filterItems.adapter?.notifyDataSetChanged()

    }
}