package com.example.ezproject.data.network.response


import com.google.gson.annotations.SerializedName

data class CanReviewResponse(
    @SerializedName("booking_id")
    val bookingId: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("result")
    val result: Boolean
)