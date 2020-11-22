package com.denimsoftware.safe_boda_challange.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.denimsoftware.safe_boda_challange.MainActivity
import com.denimsoftware.safe_boda_challange.R
import com.denimsoftware.safe_boda_challange.adapters.FollowAdapter
import com.denimsoftware.safe_boda_challange.adapters.PaginationListener
import com.denimsoftware.safe_boda_challange.dismissSnackBar
import com.denimsoftware.safe_boda_challange.networking.GitHubService
import com.denimsoftware.safe_boda_challange.networking.Routes
import com.denimsoftware.safe_boda_challange.networking.SearchModel
import com.denimsoftware.safe_boda_challange.networking.generateService
import com.denimsoftware.safe_boda_challange.showErrorMessage
import kotlinx.android.synthetic.main.fragment_user_list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

/**
 *Created by Yerimah on 11/21/2020.
 */
class UserListFragment(private val endPoint: String) : Fragment() {

    private lateinit var context: MainActivity

    var currentPage = 1
    var isLastPage = false
    var totalPage = 10
    var isLoading = false
    var itemCount = 0
    lateinit var followAdapter: FollowAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = activity as MainActivity
        followAdapter = FollowAdapter(context, LinkedList(), this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_user_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initRecyclerView()
    }

    fun loadMoreItems() {
        isLoading = true
        currentPage++
        followAdapter.addLoading()
        retrieveUserData(context, constraintLayout)
    }

    private fun retrieveUserData(activity: MainActivity, constraintLayout: ConstraintLayout) {

        if (endPoint == Routes.FOLLOWING_END_POINT) {
            if (activity.viewModel.followUserName == activity.viewModel.currentUserName) {
                hideEmptyState()
                if (activity.viewModel.followingList.isNotEmpty()) {
                    initItems(activity.viewModel.followingList)
                }
            }
        }
        else {
            if (activity.viewModel.followUserName == activity.viewModel.currentUserName) {
                hideEmptyState()
                if (activity.viewModel.followersList.isNotEmpty()) {
                    initItems(activity.viewModel.followersList)
                }
            }
        }

        val dismissAction = View.OnClickListener { dismissSnackBar() }
        val retryAction = View.OnClickListener { retrieveUserData(activity, constraintLayout) }
        val call = generateService(GitHubService::class.java).
        retrieveFollow("application/vnd.github.v3+json",
            Routes.USER_END_POINT + activity.viewModel.currentUserName + "/" + endPoint, currentPage, 10)

        call.enqueue(object : Callback<List<SearchModel>> {

            override fun onResponse(call: Call<List<SearchModel>>,
                response: Response<List<SearchModel>>) {
                if (response.isSuccessful) {
                    response.body().let {
                        if (!response.body().isNullOrEmpty()) {
                            hideEmptyState()
                            activity.viewModel.followUserName = activity.viewModel.currentUserName
                            if (endPoint == Routes.FOLLOWING_END_POINT) {
                                activity.viewModel.followingList.clear()
                                activity.viewModel.followingList.addAll(response.body()!!)
                            } else {
                                activity.viewModel.followersList.clear()
                                activity.viewModel.followersList.addAll(response.body()!!)
                            }
                            initItems(response.body()!!)
                            dismissSnackBar()
                        } else {
                            showEmptyState()
                            showErrorMessage(activity, constraintLayout, getString(R.string.follow_empty_message), dismissAction, activity.resources.getString(R.string.dismiss_text))
                        }
                    }
                } else {
                    showEmptyState()
                    showErrorMessage(activity, constraintLayout,
                        getString(R.string.follow_empty_message), retryAction, activity.resources.getString(R.string.retry_text))
                }
            }

            override fun onFailure(call: Call<List<SearchModel>>, t: Throwable) {
                showEmptyState()
                showErrorMessage(activity, constraintLayout,
                    activity.resources.getString(R.string.network_error_text), retryAction, activity.resources.getString(R.string.retry_text))
            }
        })
    }

    private fun initItems(userList: List<SearchModel>) {

        isLoading = false
        followAdapter.removeLoading()
        followAdapter.addItems(userList)
    }

    private fun initRecyclerView() {

        usersRecyclerView.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        usersRecyclerView.layoutManager = layoutManager
        usersRecyclerView.adapter = followAdapter
        retrieveUserData(context, constraintLayout)
        usersRecyclerView.addOnScrollListener(PaginationListener(layoutManager, this))
    }

    override fun onStart() {
        super.onStart()
        itemCount = 0
        currentPage = 1
        isLastPage = false
        followAdapter.clear()
        retrieveUserData(context, constraintLayout)
    }

    private fun showEmptyState() {
        if (followAdapter.itemCount < 2) {
            if (emptyStateImage != null) {
                emptyStateImage.visibility = View.VISIBLE
                usersRecyclerView.visibility = View.GONE
            }
        }
    }

    private fun hideEmptyState() {
        if (emptyStateImage != null) {
            emptyStateImage.visibility = View.GONE
            usersRecyclerView.visibility = View.VISIBLE
        }
    }
}