package com.dragonguard.android.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class CompareAdapter (fragmentActivity: FragmentActivity, token: String) : FragmentStateAdapter(fragmentActivity) {

    fun addFragment(fragment: Fragment) {
        fragments.add(fragment)
        notifyDataSetChanged()
    }

    fun removeAll() {
        fragments.clear()
        notifyDataSetChanged()
    }

    private val fragments: MutableList<Fragment> = mutableListOf()
    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }

}