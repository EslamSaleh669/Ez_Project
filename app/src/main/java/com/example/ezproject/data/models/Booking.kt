package com.example.ezproject.data.models


import com.google.gson.annotations.SerializedName

data class Booking(
    @SerializedName("adults")
    val adults: Int,
    @SerializedName("auth")
    val auth: String,
    @SerializedName("cancel")
    val cancel: Cancel?,
    @SerializedName("cancel_message")
    val cancelMessage: String?,
    @SerializedName("check_in")
    val checkIn: String,
    @SerializedName("check_out")
    val checkOut: String,
    @SerializedName("childrens")
    val childrens: Int,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("date_end")
    val dateEnd: String,
    @SerializedName("date_start")
    val dateStart: String,
    @SerializedName("day_price")
    val dayPrice: Float,
    @SerializedName("ezuru_fee")
    val ezuruFee: Double,
    @SerializedName("fee")
    val fee: Int,
    @SerializedName("gateway")
    val gateway: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("owner_id")
    val ownerId: Int,

    @SerializedName("price")
    val price: Float,
    @SerializedName("status")
    var status: Int,
    @SerializedName("tax")
    val tax: Double,
    @SerializedName("token")
    val token: String,
    @SerializedName("tourism")
    val tourism: Double,
    @SerializedName("unit")
    val unit: Unit,
    @SerializedName("unit_id")
    val unitId: Int,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("user_id")
    val userId: Int
) {
    data class Cancel(
        @SerializedName("booking_id")
        val bookingId: Int,
        @SerializedName("cancel_id")
        val cancelId: Int,
        @SerializedName("created_at")
        val createdAt: String,
        @SerializedName("ezuru_fee")
        val ezuruFee: Int,
        @SerializedName("id")
        val id: Int,
        @SerializedName("object")
        val objectX: Any,
        @SerializedName("price")
        val price: Float,
        @SerializedName("price_fee")
        val priceFee: Float,
        @SerializedName("tax")
        val tax: Int,
        @SerializedName("tourism")
        val tourism: Int,
        @SerializedName("updated_at")
        val updatedAt: String
    )

}