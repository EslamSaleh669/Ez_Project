package com.example.ezproject.data.network.response


import com.google.gson.annotations.SerializedName

data class AddBookingReponse(
    @SerializedName("message")
    val message: String,
    @SerializedName("response")
    val response: Response,
    @SerializedName("status")
    val status: Int
) {
    data class Response(
        @SerializedName("adults")
        val adults: String,
        @SerializedName("childrens")
        val childrens: String,
        @SerializedName("created_at")
        val createdAt: String,
        @SerializedName("date_end")
        val dateEnd: String,
        @SerializedName("date_start")
        val dateStart: String,
        @SerializedName("day_price")
        val dayPrice: String,
        @SerializedName("ezuru_fee")
        val ezuruFee: String,
        @SerializedName("fee")
        val fee: String,
        @SerializedName("gateway")
        val gateway: String,
        @SerializedName("id")
        val id: Int,
        @SerializedName("owner_id")
        val ownerId: String,
        @SerializedName("price")
        val price: String,
        @SerializedName("tax")
        val tax: String,
        @SerializedName("tourism")
        val tourism: String,
        @SerializedName("unit_id")
        val unitId: String,

        val paymentUrl: String?,
        @SerializedName("updated_at")
        val updatedAt: String,
        @SerializedName("user_id")
        val userId: Int
    )
}