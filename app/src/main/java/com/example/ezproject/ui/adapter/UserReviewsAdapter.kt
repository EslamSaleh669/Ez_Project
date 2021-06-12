package com.example.ezproject.ui.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.ezproject.R
import com.example.ezproject.data.network.response.UserReviewsResponse
import com.example.ezproject.util.Constants
import jp.wasabeef.glide.transformations.CropCircleWithBorderTransformation

class UserReviewsAdapter(
    private val reviews: ArrayList<UserReviewsResponse.Response.Review>,
    private val activity: Activity?
) : RecyclerView.Adapter<UserReviewsAdapter.UserReviewViewHolder>() {
    class UserReviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val unitImage: ImageView = itemView.findViewById(R.id.unitImage)
        val unitTitle: TextView = itemView.findViewById(R.id.unitTitle)
        val ownerName: TextView = itemView.findViewById(R.id.ownerName)
        val guestName: TextView = itemView.findViewById(R.id.guestName)
        val reviewDate: TextView = itemView.findViewById(R.id.reviewDate)
        val bookingId: TextView = itemView.findViewById(R.id.bookingId)
        val reviewContent: TextView = itemView.findViewById(R.id.reviewContent)
        val reviewRating: RatingBar = itemView.findViewById(R.id.reviewRating)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserReviewViewHolder =
        UserReviewViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.user_review_item_layout,
                parent,
                false
            )
        )

    override fun getItemCount(): Int = reviews.size

    override fun onBindViewHolder(holder: UserReviewViewHolder, position: Int) {
        Glide.with(activity!!)
            .load("${reviews[position].unit.attachments[0].image}")
            .placeholder(R.drawable.ic_default_unit).into(holder.unitImage)
        holder.bookingId.text = "Ezuru#${reviews[position].bookingId}"
        holder.guestName.text = reviews[position].guest.name
        holder.unitTitle.text = reviews[position].unit.title
        holder.ownerName.text = reviews[position].owner.name
        holder.reviewDate.text = reviews[position].createdAt.split(" ")[0]
        holder.reviewContent.text = reviews[position].review
        holder.reviewRating.rating = reviews[position].score
    }


    fun addItems(items: ArrayList<UserReviewsResponse.Response.Review>) {
        this.reviews.addAll(items)
        notifyDataSetChanged()
    }
}