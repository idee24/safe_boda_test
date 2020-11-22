package com.denimsoftware.safe_boda_challange.fragments

import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.denimsoftware.safe_boda_challange.*
import com.denimsoftware.safe_boda_challange.networking.GitHubService
import com.denimsoftware.safe_boda_challange.networking.Routes
import com.denimsoftware.safe_boda_challange.networking.UserModel
import com.denimsoftware.safe_boda_challange.networking.generateService
import kotlinx.android.synthetic.main.fragment_profile.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 *Created by Yerimah on 11/21/2020.
 */
class ProfileFragment: Fragment() {

    private lateinit var context: MainActivity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = activity as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        backIcon.setOnClickListener { context.onBackPressed() }
        retrieveUser(context, constraintLayout)
    }

    private fun loadSocialFragment() {
        context.viewModel.loadFragment(context.viewModel.getFragmentByKey(Constants.SOCIAL_FRAGMENT).fragment, context)
    }

    private fun retrieveUser(activity: MainActivity, constraintLayout: ConstraintLayout) {

        if (activity.viewModel.currentUserName == activity.viewModel.currentProfile.login) {
            initViews(activity, activity.viewModel.currentProfile)
            return
        }

        val retryAction = View.OnClickListener { retrieveUser(activity, constraintLayout) }
        val call = generateService(GitHubService::class.java).
        retrieveUser("application/vnd.github.v3+json", Routes.USER_END_POINT + activity.viewModel.currentUserName)
        call.enqueue(object : Callback<UserModel> {

            override fun onResponse(call: Call<UserModel>, response: Response<UserModel>) {
                if (response.isSuccessful) {
                    response.body().let {
                        val payload = it
                        if (payload != null) {
                            activity.viewModel.currentProfile = payload
                            initViews(activity, payload)
                        }
                        else {
                            showErrorMessage(
                                activity, constraintLayout,
                                getString(R.string.user_empty_message), retryAction,
                                activity.resources.getString(R.string.retry_text))
                        }
                    }
                } else {
                    showErrorMessage(
                        activity, constraintLayout,
                        getString(R.string.user_empty_message), retryAction,
                        activity.resources.getString(R.string.retry_text))
                }
            }

            override fun onFailure(call: Call<UserModel>, t: Throwable) {
                showErrorMessage(
                    activity, constraintLayout,
                    activity.resources.getString(R.string.network_error_text), retryAction,
                    activity.resources.getString(R.string.retry_text)
                )
            }
        })
    }

    private fun initViews(activity: MainActivity, currentUser: UserModel) {

        loadImage(
            activity,
            profileImageView,
            currentUser.avatar_url ?: "",
            R.drawable.placeholder_image
        )
        nameTextView.text = currentUser.name
        cityTextView.text = currentUser.location
        val followers =  "${currentUser.followers} Followers"
        val following =  "${currentUser.following} Following"
        followersTextView.text = followers
        followingTextView.text = following
        bioTextView.text = currentUser.bio
        pRTextView.text = currentUser.public_repos.toString()
        pGTextView.text = currentUser.public_gists.toString()
        updateDateTextView.text = currentUser.updated_at
        linkTextView.text = currentUser.url
        linkTextView.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        followingTextView.setOnClickListener { loadSocialFragment() }
        followersTextView.setOnClickListener { loadSocialFragment() }
    }
}