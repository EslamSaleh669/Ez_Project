package com.example.ezproject.data.models


import com.google.gson.annotations.SerializedName

data class UnitDetails(
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
    @SerializedName("amenities")
    val amenities: List<Amenity>,
    @SerializedName("area")
    val area: Int,
    @SerializedName("attachments")
    val attachments: List<Attachment>,
    @SerializedName("badges")
    val badges: List<Any>,
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
    @SerializedName("cpolicy")
    val cpolicy: Cpolicy,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("currency")
    val currency: String,
    @SerializedName("days_list")
    val daysList: List<Days>,
    @SerializedName("deliver_keys")
    val deliverKeys: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("featured")
    val featured: Int,
    @SerializedName("fee")
    val fee: Double,
    @SerializedName("fee_static")
    val feeStatic: Float,
    @SerializedName("fees")
    val fees: List<Fee>,
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
    @SerializedName("main_currency")
    val mainCurrency: String,
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
    @SerializedName("owner")
    val owner: Owner,
    @SerializedName("price")
    val price: Float,
    @SerializedName("rate_count")
    val rateCount: Float,
    @SerializedName("rate_score")
    val rateScore: Float,
    @SerializedName("rest")
    val rest: List<Rest>,
    @SerializedName("reviews")
    val reviews: List<Review>,
    @SerializedName("rooms")
    val rooms: Int,
    @SerializedName("status")
    val status: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("tourism")
    val tourism: Float,
    @SerializedName("tourism_static")
    val tourismStatic: Int,
    @SerializedName("type")
    val type: Int,
    @SerializedName("unit_number")
    val unitNumber: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("user_id")
    val userId: Int,
    @SerializedName("vat")
    val vat: Int,
    @SerializedName("video")
    val video: Any,
    @SerializedName("views")
    val views: List<View>,
    @SerializedName("zipcode")
    val zipcode: String
) {
    data class Amenity(
        @SerializedName("created_at")
        val createdAt: String,
        @SerializedName("description")
        val description: Any,
        @SerializedName("description_en")
        val descriptionEn: Any,
        @SerializedName("id")
        val id: Int,
        @SerializedName("name")
        val name: String,
        @SerializedName("name_en")
        val nameEn: String,
        @SerializedName("parent")
        val parent: Any,
        @SerializedName("photo")
        val photo: String,
        @SerializedName("pivot")
        val pivot: Pivot,
        @SerializedName("status")
        val status: Int,
        @SerializedName("type")
        val type: String,
        @SerializedName("updated_at")
        val updatedAt: String
    ) {
        data class Pivot(
            @SerializedName("taxonomy_id")
            val taxonomyId: Int,
            @SerializedName("unit_id")
            val unitId: Int
        )
    }

    data class Attachment(
        @SerializedName("created_at")
        val createdAt: String,
        @SerializedName("id")
        val id: Int,
        @SerializedName("image")
        val image: String,
        @SerializedName("ordr")
        val ordr: Int,
        @SerializedName("thumb")
        val thumb: String,
        @SerializedName("title")
        val title: Any,
        @SerializedName("unit_id")
        val unitId: Int,
        @SerializedName("updated_at")
        val updatedAt: String
    )

    data class Cpolicy(
        @SerializedName("before")
        val before: Int,
        @SerializedName("before_fee")
        val beforeFee: Int,
        @SerializedName("before_percent")
        val beforePercent: Int,
        @SerializedName("checkin_fee")
        val checkinFee: Int,
        @SerializedName("checkin_minus")
        val checkinMinus: Int,
        @SerializedName("checkin_out")
        val checkinOut: Int,
        @SerializedName("created_at")
        val createdAt: String,
        @SerializedName("description")
        val description: String,
        @SerializedName("description_en")
        val descriptionEn: String,
        @SerializedName("id")
        val id: Int,
        @SerializedName("name")
        val name: String,
        @SerializedName("name_en")
        val nameEn: String,
        @SerializedName("status")
        val status: Int,
        @SerializedName("updated_at")
        val updatedAt: String,
        @SerializedName("within")
        val within: Int,
        @SerializedName("within_fee")
        val withinFee: Int,
        @SerializedName("within_minus")
        val withinMinus: Int,
        @SerializedName("within_percent")
        val withinPercent: Int
    )

    data class Days(
        @SerializedName("date")
        val date: String,
        @SerializedName("id")
        val id: Int,
        @SerializedName("price")
        val price: Float,
        @SerializedName("price_before")
        val priceBefore: Float,
        @SerializedName("status")
        val status: Int,
        @SerializedName("unit_id")
        val unitId: Int
    )

    data class Fee(
        @SerializedName("amount")
        val amount: Float,
        @SerializedName("created_at")
        val createdAt: String,
        @SerializedName("fee_id")
        val feeId: Int,
        @SerializedName("fee_type")
        val feeType: String,
        @SerializedName("id")
        val id: Int,
        @SerializedName("unit_id")
        val unitId: Int,
        @SerializedName("updated_at")
        val updatedAt: String,
        @SerializedName("selectable")
        val selectable: String,
        @SerializedName("taxonomy")
        val taxonomy: Taxonomy

    )

    data class Taxonomy(

        @SerializedName("parent")
        val parent: Any? = null,

        @SerializedName("description_en")
        val descriptionEn: Any? = null,

        @SerializedName("region_id")
        val regionId: Any? = null,

        @SerializedName("description")
        val description: Any? = null,

        @SerializedName("photo")
        val photo: Any? = null,

        @SerializedName("created_at")
        val createdAt: String? = null,

        @SerializedName("type")
        val type: String? = null,

        @SerializedName("updated_at")
        val updatedAt: String? = null,

        @SerializedName("taxArrange")
        val taxArrange: Any? = null,

        @SerializedName("name")
        val name: String? = null,

        @SerializedName("id")
        val id: Int? = null,

        @SerializedName("name_en")
        val nameEn: String? = null,

        @SerializedName("status")
        val status: Int? = null
    )

    data class Owner(
        @SerializedName("accept")
        val accept: Int,
        @SerializedName("add_by")
        val addBy: Any,
        @SerializedName("avatar")
        val avatar: String,
        @SerializedName("city")
        val city: Int,
        @SerializedName("created_at")
        val createdAt: String,
        @SerializedName("description")
        val description: String,
        @SerializedName("email")
        val email: String,
        @SerializedName("email_verified_at")
        val emailVerifiedAt: String,
        @SerializedName("id")
        val id: Int,
        @SerializedName("lang")
        val lang: String,
        @SerializedName("mobile")
        val mobile: String,
        @SerializedName("mobile_verified_at")
        val mobileVerifiedAt: String,
        @SerializedName("name")
        val name: String,
        @SerializedName("percent")
        val percent: Int,
        @SerializedName("photoid")
        val photoid: Any,
        @SerializedName("photoid_verified_at")
        val photoidVerifiedAt: Any,
        @SerializedName("rate_count")
        val rateCount: Int,
        @SerializedName("rate_score")
        val rateScore: Float,
        @SerializedName("status")
        val status: Int,
        @SerializedName("updated_at")
        val updatedAt: String
    )

    data class Rest(
        @SerializedName("created_at")
        val createdAt: String,
        @SerializedName("description")
        val description: Any,
        @SerializedName("description_en")
        val descriptionEn: Any,
        @SerializedName("id")
        val id: Int,
        @SerializedName("name")
        val name: String,
        @SerializedName("name_en")
        val nameEn: String,
        @SerializedName("parent")
        val parent: Any,
        @SerializedName("photo")
        val photo: String,
        @SerializedName("pivot")
        val pivot: Pivot,
        @SerializedName("status")
        val status: Int,
        @SerializedName("type")
        val type: String,
        @SerializedName("updated_at")
        val updatedAt: String
    ) {
        data class Pivot(
            @SerializedName("taxonomy_id")
            val taxonomyId: Int,
            @SerializedName("unit_id")
            val unitId: Int
        )
    }

    data class Review(
        @SerializedName("booking_id")
        val bookingId: Int,
        @SerializedName("created_at")
        val createdAt: String,
        @SerializedName("guest")
        val guest: Guest,
        @SerializedName("guest_id")
        val guestId: Int,
        @SerializedName("guest_review")
        val guestReview: Any,
        @SerializedName("guest_review_date")
        val guestReviewDate: Any,
        @SerializedName("guest_score")
        val guestScore: Int,
        @SerializedName("id")
        val id: Int,
        @SerializedName("owner_id")
        val ownerId: Int,
        @SerializedName("review")
        val review: String,
        @SerializedName("score")
        val score: Float,
        @SerializedName("status")
        val status: Int,
        @SerializedName("unit_id")
        val unitId: Int,
        @SerializedName("unit_review_date")
        val unitReviewDate: String,
        @SerializedName("updated_at")
        val updatedAt: String
    ) {
        data class Guest(
            @SerializedName("accept")
            val accept: Int,
            @SerializedName("add_by")
            val addBy: Any,
            @SerializedName("avatar")
            val avatar: String,
            @SerializedName("city")
            val city: Any,
            @SerializedName("created_at")
            val createdAt: String,
            @SerializedName("description")
            val description: String,
            @SerializedName("email")
            val email: String,
            @SerializedName("email_verified_at")
            val emailVerifiedAt: String,
            @SerializedName("id")
            val id: Int,
            @SerializedName("lang")
            val lang: String,
            @SerializedName("mobile")
            val mobile: String,
            @SerializedName("mobile_verified_at")
            val mobileVerifiedAt: Any,
            @SerializedName("name")
            val name: String,
            @SerializedName("percent")
            val percent: Int,
            @SerializedName("photoid")
            val photoid: String,
            @SerializedName("photoid_verified_at")
            val photoidVerifiedAt: String,
            @SerializedName("rate_count")
            val rateCount: Int,
            @SerializedName("rate_score")
            val rateScore: Float,
            @SerializedName("status")
            val status: Int,
            @SerializedName("updated_at")
            val updatedAt: String
        )
    }

    data class View(
        @SerializedName("created_at")
        val createdAt: String,
        @SerializedName("description")
        val description: Any,
        @SerializedName("description_en")
        val descriptionEn: Any,
        @SerializedName("id")
        val id: Int,
        @SerializedName("name")
        val name: String,
        @SerializedName("name_en")
        val nameEn: String,
        @SerializedName("parent")
        val parent: Any,
        @SerializedName("photo")
        val photo: String,
        @SerializedName("pivot")
        val pivot: Pivot,
        @SerializedName("status")
        val status: Int,
        @SerializedName("type")
        val type: String,
        @SerializedName("updated_at")
        val updatedAt: String
    ) {
        data class Pivot(
            @SerializedName("taxonomy_id")
            val taxonomyId: Int,
            @SerializedName("unit_id")
            val unitId: Int
        )
    }
}