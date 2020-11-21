package com.denimsoftware.safe_boda_challange.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentManager
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.denimsoftware.safe_boda_challange.MainActivity
import com.denimsoftware.safe_boda_challange.R
import com.denimsoftware.safe_boda_challange.adapters.AppPagerAdapter
import com.denimsoftware.safe_boda_challange.networking.Routes
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_social.*

/**
 *Created by Yerimah on 11/21/2020.
 */
class SocialFragment : Fragment() {

    private lateinit var context: MainActivity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = activity as MainActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_social, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        backIcon.setOnClickListener { context.onBackPressed() }
        initViewPager(socialViewPager, socialTabs, childFragmentManager)
    }

    private fun initViewPager(viewPager: ViewPager,
                              tabs: TabLayout,
                              childFragmentManager: FragmentManager) {

        val pagerAdapter = AppPagerAdapter(childFragmentManager)

        pagerAdapter.addFragment(UserListFragment(Routes.FOLLOWERS_END_POINT), "FOLLOWERS")
        pagerAdapter.addFragment(UserListFragment(Routes.FOLLOWING_END_POINT), "FOLLOWING")
        viewPager.adapter = pagerAdapter
        tabs.setupWithViewPager(viewPager)
    }
}