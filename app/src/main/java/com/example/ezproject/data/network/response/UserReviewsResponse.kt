package com.example.ezproject.data.network.response


import com.example.ezproject.data.models.Unit
import com.google.gson.annotations.SerializedName

data class UserReviewsResponse(
    @SerializedName("message")
    val message: String,
    @SerializedName("response")
    val response: Response,
    @SerializedName("status")
    val status: Int
) {
    data class Response(
        @SerializedName("current_page")
        val currentPage: Int,
        @SerializedName("data")
        val reviews: ArrayList<Review>,
        @SerializedName("first_page_url")
        val firstPageUrl: String,
        @SerializedName("from")
        val from: Int,
        @SerializedName("last_page")
        val lastPage: Int,
        @SerializedName("last_page_url")
        val lastPageUrl: String,
        @SerializedName("next_page_url")
        val nextPageUrl: Any,
        @SerializedName("path")
        val path: String,
        @SerializedName("per_page")
        val perPage: Int,
        @SerializedName("prev_page_url")
        val prevPageUrl: Any,
        @SerializedName("to")
        val to: Int,
        @SerializedName("total")
        val total: Int
    ) {
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
            @SerializedName("owner")
            val owner: Owner,
            @SerializedName("owner_id")
            val ownerId: Int,
            @SerializedName("review")
            val review: String,
            @SerializedName("score")
            val score: Float,
            @SerializedName("status")
            val status: Int,
            @SerializedName("unit")
            val unit: Unit,
            @SerializedName("unit_id")
            val unitId: Int,
            @SerializedName("unit_review_date")
            val unitReviewDate: Any,
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
                val rateScore: Double,
                @SerializedName("status")
                val status: Int,
                @SerializedName("updated_at")
                val updatedAt: String
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
                val rateScore: Int,
                @SerializedName("status")
                val status: Int,
                @SerializedName("updated_at")
                val updatedAt: String
            )

        }
    }
}