package com.denimsoftware.safe_boda_challange.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.denimsoftware.safe_boda_challange.MainActivity
import com.denimsoftware.safe_boda_challange.R
import com.denimsoftware.safe_boda_challange.adapters.UserAdapter
import com.denimsoftware.safe_boda_challange.dismissSnackBar
import com.denimsoftware.safe_boda_challange.networking.*
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = activity as MainActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_user_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        retrieveUserData(context, constraintLayout)
    }

    private fun retrieveUserData(activity: MainActivity,
                                constraintLayout: ConstraintLayout) {

        val dismissAction = View.OnClickListener { dismissSnackBar() }
        val retryAction = View.OnClickListener { retrieveUserData(activity, constraintLayout) }
        val call = generateService(GitHubService::class.java).
        retrieveFollow("application/vnd.github.v3+json",
            Routes.USER_END_POINT + activity.viewModel.currentUserName + "/" + endPoint)

        call.enqueue(object: Callback<List<SearchModel>> {

            override fun onResponse(call: Call<List<SearchModel>>, response: Response<List<SearchModel>>) {
                if (response.isSuccessful) {
                    response.body().let {
                        if (!response.body().isNullOrEmpty()) {
                            hideEmptyState()
                            initRecyclerView(response.body()!!)
                            dismissSnackBar()
                        }
                        else {
                            showEmptyState()
                            showErrorMessage(activity, constraintLayout,
                                getString(R.string.user_empty_message), dismissAction,
                                activity.resources.getString(R.string.dismiss_text))
                        }
                    }
                }
                else {
                    showEmptyState()
                    showErrorMessage(activity, constraintLayout,
                        getString(R.string.user_empty_message), retryAction,
                        activity.resources.getString(R.string.retry_text))
                }
            }

            override fun onFailure(call: Call<List<SearchModel>>, t: Throwable) {
                showEmptyState()
                showErrorMessage(activity, constraintLayout,
                    activity.resources.getString(R.string.network_error_text), retryAction,
                    activity.resources.getString(R.string.retry_text))
            }
        })
    }

    private fun initRecyclerView(searchList: List<SearchModel>) {
        val layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context,
            RecyclerView.VERTICAL, false)
        usersRecyclerView.layoutManager = layoutManager
        usersRecyclerView.adapter = UserAdapter(context, LinkedList(searchList), false)
    }

    private fun showEmptyState() {
        emptyStateImage.visibility = View.VISIBLE
        usersRecyclerView.visibility = View.GONE
    }

    private fun hideEmptyState() {
        emptyStateImage.visibility = View.GONE
        usersRecyclerView.visibility = View.VISIBLE
    }
}