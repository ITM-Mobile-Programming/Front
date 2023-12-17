package com.hwido.pieceofdayfront

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hwido.pieceofdayfront.datamodel.FriendData

class FriendListAdapter : RecyclerView.Adapter<FriendListAdapter.FriendViewHolder>() {

    private val friendList = mutableListOf<FriendData>()

    // ViewHolder class
    class FriendViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val friendImage: ImageView = itemView.findViewById(R.id.main_friendlistformat_friendImage)
        val friendNameTextView: TextView = itemView.findViewById(R.id.main_friendlistformat_friendName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.main_friendlistformat, parent, false)
        return FriendViewHolder(view)
    }

    override fun onBindViewHolder(holder: FriendViewHolder, position: Int) {
        val currentFriend = friendList[position]

        Glide.with(holder.friendImage)
            .load(currentFriend.profileUrl)
            .into(holder.friendImage)

        holder.friendNameTextView.text = currentFriend.nickName
    }

    override fun getItemCount(): Int {
        return friendList.size
    }

    fun setFriendList(data: List<FriendData>) {
        friendList.clear()
        friendList.addAll(data)
        notifyDataSetChanged()
    }
}