package com.dragonguard.android.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dragonguard.android.fragment.AllRankingsFragment

class FragmentAdapter (fragment: Fragment, private val token: String) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = fragmentList.size

    private val fragmentList = listOf<String>("total", "company", "university", "high_school", "etc")

    override fun createFragment(position: Int): Fragment {
        return AllRankingsFragment(token, fragmentList[position])
    }
}