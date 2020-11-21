package com.denimsoftware.safe_boda_challange.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.denimsoftware.safe_boda_challange.Constants
import com.denimsoftware.safe_boda_challange.MainActivity
import com.denimsoftware.safe_boda_challange.R
import com.denimsoftware.safe_boda_challange.loadImage
import com.denimsoftware.safe_boda_challange.networking.SearchModel
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*

/**
 *Created by Yerimah on 11/21/2020.
 */
class UserAdapter(private val activity: MainActivity, private val searchList: LinkedList<SearchModel>, private val isUserProfile: Boolean):
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.user_item, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        loadImage(activity, holder.imageView, searchList[position].avatar_url, R.drawable.placeholder_image)
        holder.nameTextView.text = searchList[position].login
        holder.idTextView.text = searchList[position].id.toString()
        if (isUserProfile) {
            holder.itemView.setOnClickListener {
                activity.viewModel.currentUserName = searchList[position].login
                activity.viewModel.loadFragment(activity.viewModel.getFragmentByKey(Constants.PROFILE_FRAGMENT).fragment, activity)
            }
        }
    }

    override fun getItemCount(): Int = searchList.size


    inner class UserViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val imageView: CircleImageView = itemView.findViewById(R.id.userImageView)
        val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        val idTextView: TextView = itemView.findViewById(R.id.idTextView)
    }
}