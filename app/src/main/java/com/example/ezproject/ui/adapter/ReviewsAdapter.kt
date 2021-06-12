package com.example.ezproject.ui.adapter

import android.app.Activity
import android.media.Image
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
import com.example.ezproject.data.network.response.ReviewsResponse
import com.example.ezproject.util.Constants
import com.example.ezproject.util.extensions.loadUrl
import jp.wasabeef.glide.transformations.CropCircleWithBorderTransformation

class ReviewsAdapter(
    private val reviews: List<ReviewsResponse.Response.Data>,
    private val activity: Activity?
) :
    RecyclerView.Adapter<ReviewsAdapter.ReviewViewHolder>() {

    class ReviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userImage: ImageView = itemView.findViewById(R.id.reviewUserImage)
        val username: TextView = itemView.findViewById(R.id.reviewUsername)
        val reviewDate: TextView = itemView.findViewById(R.id.reviewDate)
        val reviewContent: TextView = itemView.findViewById(R.id.reviewContent)
        val reviewRating: RatingBar = itemView.findViewById(R.id.reviewRating)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder =
        ReviewViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.review_item_layout,
                parent,
                false
            )
        )

    override fun getItemCount(): Int = reviews.size

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        Glide.with(activity!!)
            .load("${Constants.STORAGE_URL}${reviews[position].guest.avatar}")
            .placeholder(R.drawable.ic_account)
            .circleCrop().into(holder.userImage)
        holder.username.text = reviews[position].guest.name
        holder.reviewDate.text = reviews[position].createdAt.split(" ")[0]
        holder.reviewContent.text = reviews[position].review
        holder.reviewRating.rating = reviews[position].score
    }
}