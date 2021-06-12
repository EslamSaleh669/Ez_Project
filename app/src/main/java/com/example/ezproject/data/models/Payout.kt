package com.example.ezproject.data.models


import com.google.gson.annotations.SerializedName

data class Payout(
    @SerializedName("amount")
    val amount: Amount,
    @SerializedName("amount_status")
    val amountStatus: String,
    @SerializedName("cancel_amount")
    val cancelAmount: CancelAmount,
    @SerializedName("hasCancel")
    val hasCancel: Boolean,
    @SerializedName("object")
    val objectX: Object,
    @SerializedName("unit")
    val unit: Unit
) {
    data class Amount(
        @SerializedName("amount")
        val amount: Float,
        @SerializedName("fee")
        val fee: String
    )

    data class CancelAmount(
        @SerializedName("amount")
        val amount: String,
        @SerializedName("fee")
        val fee: String
    )

    data class Object(
        @SerializedName("adults")
        val adults: String,
        @SerializedName("auth")
        val auth: Any,
        @SerializedName("cancel")
        val cancel: Any,
        @SerializedName("cancel_message")
        val cancelMessage: Any,
        @SerializedName("check_in")
        val checkIn: String,
        @SerializedName("check_out")
        val checkOut: String,
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
        val id: String,
        @SerializedName("owner_id")
        val ownerId: String,
        @SerializedName("payerid")
        val payerid: Any,
        @SerializedName("price")
        val price: String,
        @SerializedName("status")
        val status: String,
        @SerializedName("tax")
        val tax: String,
        @SerializedName("token")
        val token: Any,
        @SerializedName("tourism")
        val tourism: String,
        @SerializedName("unit")
        val unit: Unit,
        @SerializedName("unit_id")
        val unitId: String,
        @SerializedName("updated_at")
        val updatedAt: String,
        @SerializedName("user_id")
        val userId: String
    ) {
        data class Unit(
            @SerializedName("address")
            val address: String,
            @SerializedName("allow_animals")
            val allowAnimals: String,
            @SerializedName("allow_childrens")
            val allowChildrens: String,
            @SerializedName("allow_extra")
            val allowExtra: String,
            @SerializedName("allow_infants")
            val allowInfants: String,
            @SerializedName("area")
            val area: String,
            @SerializedName("balacons")
            val balacons: String,
            @SerializedName("bank_account")
            val bankAccount: String,
            @SerializedName("bank_number")
            val bankNumber: String,
            @SerializedName("bathrooms")
            val bathrooms: String,
            @SerializedName("beds")
            val beds: String,
            @SerializedName("building_number")
            val buildingNumber: String,
            @SerializedName("cancle_policy")
            val canclePolicy: Any,
            @SerializedName("checkin")
            val checkin: String,
            @SerializedName("checkout")
            val checkout: String,
            @SerializedName("child_type")
            val childType: Any,
            @SerializedName("city")
            val city: String,
            @SerializedName("contract_image")
            val contractImage: String,
            @SerializedName("country")
            val country: String,
            @SerializedName("created_at")
            val createdAt: String,
            @SerializedName("currency")
            val currency: String,
            @SerializedName("deliver_keys")
            val deliverKeys: String,
            @SerializedName("description")
            val description: String,
            @SerializedName("featured")
            val featured: String,
            @SerializedName("fee")
            val fee: Double,
            @SerializedName("fee_static")
            val feeStatic: String,
            @SerializedName("floor_number")
            val floorNumber: String,
            @SerializedName("get_keys")
            val getKeys: String,
            @SerializedName("government")
            val government: String,
            @SerializedName("guests")
            val guests: String,
            @SerializedName("id")
            val id: String,
            @SerializedName("latitude")
            val latitude: String,
            @SerializedName("longitude")
            val longitude: String,
            @SerializedName("max_childrens")
            val maxChildrens: String,
            @SerializedName("max_days")
            val maxDays: String,
            @SerializedName("max_extra")
            val maxExtra: String,
            @SerializedName("min_days")
            val minDays: String,
            @SerializedName("min_guests")
            val minGuests: String,
            @SerializedName("notes")
            val notes: String,
            @SerializedName("price")
            val price: String,
            @SerializedName("rate_count")
            val rateCount: String,
            @SerializedName("rate_score")
            val rateScore: String,
            @SerializedName("rooms")
            val rooms: String,
            @SerializedName("status")
            val status: String,
            @SerializedName("title")
            val title: String,
            @SerializedName("tourism")
            val tourism: String,
            @SerializedName("tourism_static")
            val tourismStatic: String,
            @SerializedName("type")
            val type: String,
            @SerializedName("unit_number")
            val unitNumber: String,
            @SerializedName("updated_at")
            val updatedAt: String,
            @SerializedName("user_id")
            val userId: String,
            @SerializedName("vat")
            val vat: String,
            @SerializedName("video")
            val video: String,
            @SerializedName("zipcode")
            val zipcode: String
        )
    }

    data class Unit(
        @SerializedName("currency")
        val currency: String,
        @SerializedName("id")
        val id: String,
        @SerializedName("title")
        val title: String
    )
}