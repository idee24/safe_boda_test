package com.denimsoftware.safe_boda_challange.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.denimsoftware.safe_boda_challange.Constants
import com.denimsoftware.safe_boda_challange.MainActivity
import com.denimsoftware.safe_boda_challange.R
import com.denimsoftware.safe_boda_challange.fragments.UserListFragment
import com.denimsoftware.safe_boda_challange.loadImage
import com.denimsoftware.safe_boda_challange.networking.SearchModel
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*

/**
 *Created by Yerimah on 11/22/2020.
 */
class FollowAdapter(private val activity: MainActivity,
                    private val searchList: LinkedList<SearchModel>,
                    private val userListFragment: UserListFragment):
    RecyclerView.Adapter<FollowAdapter.FollowViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowViewHolder {

        return FollowViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.user_item, parent, false))
    }

    fun addItems(items: List<SearchModel>) {
        searchList.addAll(items)
        notifyDataSetChanged()
    }

    fun clear() {
        searchList.clear()
        notifyDataSetChanged()
    }

    fun addLoading() {
        searchList.add(SearchModel())
        notifyItemInserted(searchList.lastIndex)
    }

    fun removeLoading() {
        if (searchList.isNotEmpty()) {
            searchList.removeAt(searchList.lastIndex)
            notifyItemRemoved(searchList.lastIndex)
        }
    }

    override fun onBindViewHolder(holder: FollowAdapter.FollowViewHolder, position: Int) {

        if (!userListFragment.isLoading) {
            loadImage(activity, holder.imageView, searchList[position].avatar_url ?: "", R.drawable.placeholder_image)
            holder.nameTextView.text = searchList[position].login
            holder.idTextView.text = searchList[position].id.toString()
            holder.itemView.setOnClickListener {
                activity.viewModel.currentUserName = searchList[position].login ?: ""
                activity.viewModel.loadFragment(activity.viewModel.getFragmentByKey(Constants.PROFILE_FRAGMENT).fragment, activity)
            }
        }
        else {
            if (position == searchList.lastIndex) {
                holder.mainLayout.visibility = View.GONE
                holder.loadingLayout.visibility = View.VISIBLE
            }
        }
    }


    override fun getItemCount(): Int = searchList.size

    inner class FollowViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        val imageView: CircleImageView = itemView.findViewById(R.id.userImageView)
        val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        val idTextView: TextView = itemView.findViewById(R.id.idTextView)
        val loadingLayout: ConstraintLayout = itemView.findViewById(R.id.loadingLayout)
        val mainLayout: ConstraintLayout = itemView.findViewById(R.id.mainLayout)
    }
}