package com.dragonguard.android.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dragonguard.android.fragment.*

class FragmentAdapter (fragment: Fragment, private val token: String) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = fragmentList.size

    private val fragmentList = listOf<String>("사용자 전체", "조직 전체", "회사", "대학교", "고등학교", "ETC")

    override fun createFragment(position: Int): Fragment {
        val fragmentTag = fragmentList[position]
        return when (fragmentTag) {
            "사용자 전체" -> AllRankingsFragment(token, fragmentTag)
            "조직 전체" -> TotalOrganizationFragment(token)
            "회사" -> CompanyFragment(token)
            "대학교" -> UniversityFragment(token)
            "고등학교" -> HighSchoolFragment(token)
            "ETC" -> EtcFragment(token)
            else -> throw IllegalArgumentException("Invalid fragment tag: $fragmentTag")
        }
    }
}