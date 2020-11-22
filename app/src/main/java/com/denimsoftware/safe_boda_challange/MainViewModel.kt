package com.denimsoftware.safe_boda_challange

import android.app.Application
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import com.denimsoftware.safe_boda_challange.fragments.HomeFragment
import com.denimsoftware.safe_boda_challange.fragments.ProfileFragment
import com.denimsoftware.safe_boda_challange.fragments.SocialFragment
import com.denimsoftware.safe_boda_challange.networking.*
import java.util.*
import kotlin.collections.LinkedHashMap

/**
 *Created by Yerimah on 7/10/2020.
 */
class MainViewModel(application: Application): AndroidViewModel(application) {

    private var backStack = LinkedHashMap<String, AppFragment>()
    private var fragmentMap = LinkedHashMap<Fragment, AppFragment>()
    private lateinit var currentFragment: Fragment
    var searchList = LinkedList<SearchModel>()
    var followingList = LinkedList<SearchModel>()
    var followersList = LinkedList<SearchModel>()
    var followUserName = ""
    var currentUserName = ""
    var currentSearchString = ""
    var currentProfile = UserModel()

    fun initFragmentNavStructure(activity: MainActivity) {

        val homeFragment = AppFragment(activity, Constants.HOME_FRAGMENT, HomeFragment(), null)
        deployFragment(homeFragment, Constants.HOME_FRAGMENT)

        val profileFragment = AppFragment(activity, Constants.PROFILE_FRAGMENT, ProfileFragment(), homeFragment)
        deployFragment(profileFragment, Constants.PROFILE_FRAGMENT)

        val socialFragment = AppFragment(activity, Constants.SOCIAL_FRAGMENT, SocialFragment(), profileFragment)
        deployFragment(socialFragment, Constants.SOCIAL_FRAGMENT)
    }

    private fun deployFragment(gigFragment: AppFragment, fragmentName: String) {
        backStack[fragmentName] = gigFragment
        fragmentMap[gigFragment.fragment] = gigFragment
    }

    fun loadFragment(fragment: Fragment, context: MainActivity) {

        dismissSnackBar()
        val transaction = context.supportFragmentManager.beginTransaction()
        transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        currentFragment = fragment
        transaction.commitAllowingStateLoss()
    }

    fun getFragmentByKey(key: String): AppFragment {
        return backStack[key] ?: backStack[Constants.HOME_FRAGMENT]!!
    }
}