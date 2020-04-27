package com.boss.login.onboarding

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

// Fragment Manager is used to manage actions of fragments
class FragmentAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {
    // add ? after the return type 'Fragment' of the below function
    // it will mean that this function can return null values as well
    override fun getItem(position: Int): Fragment? {
        // 0,1
        when (position) {
            0 -> {
                return OnBoardingOneFragment()
            }
            1 -> {
                return OnBoardingTwoFragment()
            }
            else -> {
                return null;
            }
        }
    }

    override fun getCount(): Int {
        return 2
    }
}