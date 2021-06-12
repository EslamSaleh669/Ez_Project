package com.example.ezproject.data.network.response


import com.example.ezproject.data.models.Booking
import com.google.gson.annotations.SerializedName

data class BookingResponse(
    @SerializedName("message")
    val message: String,
    @SerializedName("response")
    val bookings: ArrayList<Booking>,
    @SerializedName("status")
    val status: Int
)