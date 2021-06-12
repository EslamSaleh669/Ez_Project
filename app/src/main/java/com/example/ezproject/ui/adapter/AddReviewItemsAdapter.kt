package com.example.ezproject.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ezproject.R
import com.example.ezproject.data.network.response.ReviewOptionsReponse

class AddReviewItemsAdapter(private val reviewOptions: List<ReviewOptionsReponse.Response.ReviewOption>) :
    RecyclerView.Adapter<AddReviewItemsAdapter.ReviewItemViewHolder>() {

    class ReviewItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemTitle: TextView = itemView.findViewById(R.id.itemTitle)
        val itemRating: RatingBar = itemView.findViewById(R.id.itemRating)
        val itemReview: EditText = itemView.findViewById(R.id.itemReview)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewItemViewHolder =
        ReviewItemViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.add_review_item_layout, parent, false)
        )

    override fun getItemCount(): Int = reviewOptions.size

    override fun onBindViewHolder(holder: ReviewItemViewHolder, position: Int) {
        holder.itemTitle.text = reviewOptions[position].name
    }
}