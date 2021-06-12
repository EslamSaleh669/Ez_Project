package com.example.ezproject.ui.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ezproject.R

class FriendsAdapter(private val activity: Activity?) :
    RecyclerView.Adapter<FriendsAdapter.FriendViewHolder>() {


    class FriendViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder =
        FriendViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.friends_item_layout,
                parent,
                false
            )
        )

    override fun getItemCount(): Int = 10

    override fun onBindViewHolder(holder: FriendViewHolder, position: Int) {
    }
}