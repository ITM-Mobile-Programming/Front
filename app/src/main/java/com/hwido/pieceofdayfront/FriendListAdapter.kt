package com.hwido.pieceofdayfront

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class FriendListAdapter : RecyclerView.Adapter<FriendListAdapter.FriendViewHolder>() {

    private val friendList = mutableListOf<String>() // Replace with your actual data type

    // ViewHolder class
    class FriendViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val friendImage: ImageView = itemView.findViewById(R.id.main_diaryformat_diaryImage)
        val friendNameTextView: TextView = itemView.findViewById(R.id.main_diaryformat_friendName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.main_friendlistformat, parent, false)
        return FriendViewHolder(view)
    }

    override fun onBindViewHolder(holder: FriendViewHolder, position: Int) {
        val currentFriend = friendList[position]

        // Example: Load an image using Glide, replace it with your image loading logic
        Glide.with(holder.friendImage)
            .load("https://example.com/image.jpg") // Replace with your image URL or resource
            .into(holder.friendImage)

        holder.friendNameTextView.text = currentFriend
    }

    override fun getItemCount(): Int {
        return friendList.size
    }

    fun setFriendList(data: List<String>) {
        friendList.clear()
        friendList.addAll(data)
        notifyDataSetChanged()
    }
}