package com.dragonguard.android.recycleradapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dragonguard.android.fragment.AllRankingsFragment

class CompareAdapter (fragmentActivity: FragmentActivity, token: String) : FragmentStateAdapter(fragmentActivity) {

    fun addFragment(fragment: Fragment) {
        fragments.add(fragment)
        notifyDataSetChanged()
    }

    private val fragments: MutableList<Fragment> = mutableListOf()
    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }

}