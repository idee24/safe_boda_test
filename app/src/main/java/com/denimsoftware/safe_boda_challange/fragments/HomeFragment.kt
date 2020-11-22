package com.denimsoftware.safe_boda_challange.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.denimsoftware.safe_boda_challange.*
import com.denimsoftware.safe_boda_challange.adapters.UserAdapter
import com.denimsoftware.safe_boda_challange.networking.*
import kotlinx.android.synthetic.main.fragment_home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

/**
 *Created by Yerimah on 7/10/2020.
 */
class HomeFragment : Fragment() {

    private lateinit var context: MainActivity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = activity as MainActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        searchField.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                if (searchField.text.toString().trim().isNotEmpty()) {
                    retrieveUsers(context, constraintLayout, searchField.text.toString().replace(" ", "+").toLowerCase())
                }
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {  }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {  }
        })
    }

    fun retrieveUsers(activity: MainActivity,
                      constraintLayout: ConstraintLayout,
                      searchKey: String) {

        if (searchKey == activity.viewModel.currentSearchString && activity.viewModel.searchList.isNotEmpty()) {
            hideEmptyState()
            initRecyclerView(activity.viewModel.searchList)
            return
        }

        val dismissAction = View.OnClickListener { dismissSnackBar() }
        val retryAction = View.OnClickListener { retrieveUsers(activity, constraintLayout, searchKey) }

        val call = generateService(GitHubService::class.java).retrieveUsers(searchKey)
        call.enqueue(object: Callback<SearchResponse> {

            override fun onResponse(call: Call<SearchResponse>, response: Response<SearchResponse>) {
                if (response.isSuccessful) {
                    response.body().let {
                        val payload = it
                        if (!payload?.items.isNullOrEmpty()) {
                            hideEmptyState()
                            activity.viewModel.currentSearchString = searchKey
                            activity.viewModel.searchList.clear()
                            if (payload?.items?.size?: 0 > 10) {
                                activity.viewModel.searchList.addAll(payload?.items!!.subList(0, 10))
                            }
                            else {
                                activity.viewModel.searchList.addAll(payload?.items!!)
                            }
                            initRecyclerView(activity.viewModel.searchList)
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

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                showEmptyState()
                showErrorMessage(activity, constraintLayout,
                    activity.resources.getString(R.string.network_error_text), retryAction,
                    activity.resources.getString(R.string.retry_text))
            }
        })
    }


    private fun showEmptyState() {
        if (emptyStateImage != null) {
            emptyStateImage.visibility = View.VISIBLE
            userRecyclerView.visibility = View.GONE
        }
    }

    private fun hideEmptyState() {
        if (emptyStateImage != null) {
            emptyStateImage.visibility = View.GONE
            userRecyclerView.visibility = View.VISIBLE
        }
    }


    private fun initRecyclerView(searchList: LinkedList<SearchModel>) {
        val layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context,
            RecyclerView.VERTICAL, false)
        userRecyclerView.layoutManager = layoutManager
        userRecyclerView.adapter = UserAdapter(context, searchList)
    }
}