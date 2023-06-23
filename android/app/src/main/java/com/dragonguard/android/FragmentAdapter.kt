package com.dragonguard.android

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dragonguard.android.fragment.AllRankingsFragment

class FragmentAdapter (fragment: Fragment, token: String) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = fragmentList.size

    private val fragmentList = listOf<Fragment>(AllRankingsFragment(token, "total"), AllRankingsFragment(token, "company"), AllRankingsFragment(token, "university"),
        AllRankingsFragment(token, "high_school"), AllRankingsFragment(token, "etc"))

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }
}