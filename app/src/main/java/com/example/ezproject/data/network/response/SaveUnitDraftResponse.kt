package com.example.ezproject.data.network.response


import com.google.gson.annotations.SerializedName

data class SaveUnitDraftResponse(
    @SerializedName("message")
    val message: Message,
    @SerializedName("status")
    val status: Int
) {
    data class Message(
        @SerializedName("address")
        val address: Any,
        @SerializedName("allow_animals")
        val allowAnimals: Int,
        @SerializedName("allow_childrens")
        val allowChildrens: Int,
        @SerializedName("allow_extra")
        val allowExtra: Int,
        @SerializedName("allow_infants")
        val allowInfants: Int,
        @SerializedName("area")
        val area: Any,
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
        @SerializedName("building_number")
        val buildingNumber: Any,
        @SerializedName("cancle_policy")
        val canclePolicy: Any,
        @SerializedName("checkin")
        val checkin: String,
        @SerializedName("checkout")
        val checkout: String,
        @SerializedName("child_type")
        val childType: Any,
        @SerializedName("city")
        val city: Any,
        @SerializedName("contract_image")
        val contractImage: Any,
        @SerializedName("country")
        val country: Any,
        @SerializedName("created_at")
        val createdAt: String,
        @SerializedName("currency")
        val currency: String,
        @SerializedName("deliver_keys")
        val deliverKeys: Any,
        @SerializedName("description")
        val description: Any,
        @SerializedName("featured")
        val featured: Int,
        @SerializedName("fee")
        val fee: Double,
        @SerializedName("fee_static")
        val feeStatic: Int,
        @SerializedName("floor_number")
        val floorNumber: Any,
        @SerializedName("get_keys")
        val getKeys: Any,
        @SerializedName("government")
        val government: Any,
        @SerializedName("guests")
        val guests: Int,
        @SerializedName("id")
        val id: Int,
        @SerializedName("latitude")
        val latitude: Any,
        @SerializedName("longitude")
        val longitude: Any,
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
        val notes: Any,
        @SerializedName("price")
        val price: Int,
        @SerializedName("rate_count")
        val rateCount: Int,
        @SerializedName("rate_score")
        val rateScore: Int,
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
        val type: Any,
        @SerializedName("unit_number")
        val unitNumber: Any,
        @SerializedName("updated_at")
        val updatedAt: String,
        @SerializedName("user_id")
        val userId: Int,
        @SerializedName("vat")
        val vat: Int,
        @SerializedName("video")
        val video: Any,
        @SerializedName("zipcode")
        val zipcode: Any
    )
}