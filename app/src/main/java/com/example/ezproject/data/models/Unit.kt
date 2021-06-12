package com.example.ezproject.data.models


import com.google.gson.annotations.SerializedName

data class Unit(
    @SerializedName("address")
    val address: String,
    @SerializedName("allow_animals")
    val allowAnimals: Int,
    @SerializedName("allow_childrens")
    val allowChildrens: Int,
    @SerializedName("allow_extra")
    val allowExtra: Int,
    @SerializedName("allow_infants")
    val allowInfants: Int,
    @SerializedName("area")
    val area: Int,
    @SerializedName("attachments")
    val attachments: List<Attachment>,
    @SerializedName("balacons")
    val balacons: String,
    @SerializedName("bank_account")
    val bankAccount: Any,
    @SerializedName("bank_number")
    val bankNumber: Any,
    @SerializedName("bathrooms")
    val bathrooms: Int,
    @SerializedName("beds")
    val beds: Int,
    @SerializedName("booking_count")
    val bookingCount: Int,
    @SerializedName("building_number")
    val buildingNumber: String,
    @SerializedName("cancle_policy")
    val canclePolicy: Int,
    @SerializedName("checkin")
    val checkin: String,
    @SerializedName("checkout")
    val checkout: String,
    @SerializedName("child_type")
    val childType: Any,
    @SerializedName("city")
    val city: Int,
    @SerializedName("contract_image")
    val contractImage: String,
    @SerializedName("country")
    val country: Int,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("currency")
    val currency: String,
    @SerializedName("deliver_keys")
    val deliverKeys: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("featured")
    val featured: Int,
    @SerializedName("fee")
    val fee: Double,
    @SerializedName("fee_static")
    val feeStatic: Int,
    @SerializedName("floor_number")
    val floorNumber: String,
    @SerializedName("get_keys")
    val getKeys: String,
    @SerializedName("government")
    val government: Int,
    @SerializedName("guests")
    val guests: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("latitude")
    val latitude: String,
    @SerializedName("longitude")
    val longitude: String,
    @SerializedName("max_childrens")
    val maxChildrens: Int,
    @SerializedName("max_days")
    val maxDays: Int,
    @SerializedName("max_extra")
    val maxExtra: Int,
    @SerializedName("min_days")
    val minDays: Int,
    @SerializedName("min_guests")
    val minGuests: Int,
    @SerializedName("notes")
    val notes: String,
    @SerializedName("price")
    val price: Float,
    @SerializedName("rate_count")
    val rateCount: Int,
    @SerializedName("rate_score")
    val rateScore: Float,
    @SerializedName("rooms")
    val rooms: Int,
    @SerializedName("status")
    val status: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("tourism")
    val tourism: Int,
    @SerializedName("tourism_static")
    val tourismStatic: Int,
    @SerializedName("type")
    val type: Int,
    @SerializedName("unit_number")
    val unitNumber: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("promotion")
    val promotion: Int,
    @SerializedName("user_id")
    val userId: Int,
    @SerializedName("vat")
    val vat: Int,
    @SerializedName("video")
    val video: Any,
    @SerializedName("zipcode")
    val zipcode: String
) {
    data class Attachment(
        @SerializedName("created_at")
        val createdAt: String,
        @SerializedName("id")
        val id: Int,
        @SerializedName("image")
        val image: String,
        @SerializedName("ordr")
        val ordr: Int,
        @SerializedName("title")
        val title: Any,
        @SerializedName("unit_id")
        val unitId: Int,
        @SerializedName("updated_at")
        val updatedAt: String
    )
}