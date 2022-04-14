package com.febrian.icc.ui.screen

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.febrian.icc.ui.screen.onboarding.*

class ViewPagerAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {

    var position: Int? = 0

    val list = listOf(
        OnboardOneFragment(),
        OnboardTwoFragment(),
        OnboardThreeFragment(),
        OnboardFourthFragment(),
        OnboardFiveFragment()

    )

    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): Fragment {
        this.position = position
        return list[position]
    }

    @JvmName("getPosition1")
    fun getPosition(): Int? {
        return position
    }
}