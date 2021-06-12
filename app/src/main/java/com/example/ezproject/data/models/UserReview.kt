package com.example.ezproject.data.models

import com.google.gson.annotations.SerializedName

class UserReview(
    @SerializedName("unit_id")
    val unitId: Int,
    @SerializedName("owner_id")
    val ownerId: Int,
    @SerializedName("guest_id")
    val guestId: Int,
    @SerializedName("booking_id")
    val bookingId: Int,
    val type: String,
    val review: String,
    val Items: ArrayList<ReviewItem>
) {
    class ReviewItem(
        val name: String,
        val rating: Float,
        val note: String
    )
}