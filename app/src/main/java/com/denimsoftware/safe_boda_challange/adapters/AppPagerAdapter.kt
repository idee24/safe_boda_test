package com.denimsoftware.safe_boda_challange.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import java.util.*

/**
 *Created by Yerimah on 11/21/2020.
 */
class AppPagerAdapter(childFragmentManager: FragmentManager):
    FragmentPagerAdapter(childFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val fragmentList = LinkedList<Fragment>()
    private val fragmentTitles = LinkedList<String>()

    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getCount(): Int = fragmentList.size

    fun addFragment(fragment: Fragment, fragmentTitle: String) {
        fragmentList.add(fragment)
        fragmentTitles.add(fragmentTitle)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return fragmentTitles[position]
    }
}