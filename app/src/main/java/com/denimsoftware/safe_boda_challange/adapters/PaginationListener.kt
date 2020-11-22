package com.denimsoftware.safe_boda_challange.adapters

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.denimsoftware.safe_boda_challange.fragments.UserListFragment


/**
 *Created by Yerimah on 11/22/2020.
 */
class PaginationListener(private val layoutManager: LinearLayoutManager,
                         private val fragment: UserListFragment): RecyclerView.OnScrollListener() {

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        val visibleItemCount = layoutManager.childCount
        val totalItemCount = layoutManager.itemCount
        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
        if (!fragment.isLoading && !fragment.isLastPage) {
            if (visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0 && totalItemCount >= 10) {
                fragment.loadMoreItems()
            }
        }
    }

}