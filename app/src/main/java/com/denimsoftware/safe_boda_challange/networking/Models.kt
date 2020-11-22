package com.denimsoftware.safe_boda_challange.networking

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.gson.annotations.SerializedName

/**
 *Created by Yerimah on 7/10/2020.
 */

class AppFragment(
    var activity: AppCompatActivity,
    var fragmentName: String,
    var fragment: Fragment,
    var previousFragment: AppFragment?
)

data class SearchResponse(
    var items: List<SearchModel>
)

data class SearchModel(
    var login: String? = "",
    var id: Int? = 0,
    var avatar_url: String? = "",
    var url: String? = "",
)

data class UserModel(
    var login: String? = "",
    var id: Int? = 0,
    var avatar_url: String?= "",
    var url: String?= "",
    var name: String?= "",
    var location: String? = "",
    var public_repos: Int? = 0,
    var public_gists: Int? = 0,
    var followers: Int? = 0,
    var following: Int? = 0,
    var updated_at: String? = "",
    var bio: String? = ""
)