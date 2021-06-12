package com.example.ezproject.data.network.response


import com.google.gson.annotations.SerializedName

data class AddReviewResponse(
    @SerializedName("message")
    val message: String,
    @SerializedName("response")
    val response: Response,
    @SerializedName("status")
    val status: Int
) {
    data class Response(
        @SerializedName("booking_id")
        val bookingId: String,
        @SerializedName("created_at")
        val createdAt: String,
        @SerializedName("guest_id")
        val guestId: String,
        @SerializedName("guest_review")
        val guestReview: Any,
        @SerializedName("guest_review_date")
        val guestReviewDate: Any,
        @SerializedName("guest_score")
        val guestScore: Any,
        @SerializedName("id")
        val id: Int,
        @SerializedName("owner_id")
        val ownerId: Int,
        @SerializedName("review")
        val review: String,
        @SerializedName("score")
        val score: Any,
        @SerializedName("status")
        val status: Int,
        @SerializedName("unit_id")
        val unitId: String,
        @SerializedName("unit_review_date")
        val unitReviewDate: Any,
        @SerializedName("updated_at")
        val updatedAt: String
    )
}