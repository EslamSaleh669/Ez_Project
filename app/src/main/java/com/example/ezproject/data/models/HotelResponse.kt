package com.example.ezproject.data.models

import com.google.gson.annotations.SerializedName

data class HotelResponse(

	@field:SerializedName("per_page")
	val perPage: String,

	@field:SerializedName("data")
	val data: ArrayList<Hotel>,

	@field:SerializedName("last_page")
	val lastPage: Int,

	@field:SerializedName("next_page_url")
	val nextPageUrl: String,

	@field:SerializedName("prev_page_url")
	val prevPageUrl: Any,

	@field:SerializedName("first_page_url")
	val firstPageUrl: String,

	@field:SerializedName("path")
	val path: String,

	@field:SerializedName("total")
	val total: Int,

	@field:SerializedName("last_page_url")
	val lastPageUrl: String,

	@field:SerializedName("from")
	val from: Int,

	@field:SerializedName("links")
	val links: List<HotelDlink>,

	@field:SerializedName("to")
	val to: Int,

	@field:SerializedName("current_page")
	val currentPage: Int
)

data class Hotel(

	@field:SerializedName("country")
	val country: Int,

	@field:SerializedName("rooms")
	val rooms: Int,

	@field:SerializedName("bank_number")
	val bankNumber: Any,

	@field:SerializedName("featured")
	val featured: Int,

	@field:SerializedName("notes")
	val notes: Any,

	@field:SerializedName("fee")
	val fee: Int,

	@field:SerializedName("floor_number")
	val floorNumber: String,

	@field:SerializedName("type")
	val type: Int,

	@field:SerializedName("title_type")
	val titleType: String,

	@field:SerializedName("deliver_keys")
	val deliverKeys: String,

	@field:SerializedName("fee_static")
	val feeStatic: Int,

	@field:SerializedName("unit_number")
	val unitNumber: String,

	@field:SerializedName("checkin")
	val checkin: String,

	@field:SerializedName("allow_extra")
	val allowExtra: Int,

	@field:SerializedName("price")
	val price: Int,

	@field:SerializedName("max_days")
	val maxDays: Int,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("longitude")
	val longitude: String,

	@field:SerializedName("bank_account")
	val bankAccount: Any,

	@field:SerializedName("area")
	val area: Int,

	@field:SerializedName("region_id")
	val regionId: Int,

	@field:SerializedName("vat")
	val vat: Int,

	@field:SerializedName("bathrooms")
	val bathrooms: Int,

	@field:SerializedName("allow_childrens")
	val allowChildrens: Int,

	@field:SerializedName("max_extra")
	val maxExtra: Int,

	@field:SerializedName("zipcode")
	val zipcode: String,

	@field:SerializedName("rate_count")
	val rateCount: Int,

	@field:SerializedName("user_id")
	val userId: Int,

	@field:SerializedName("allow_animals")
	val allowAnimals: Int,

	@field:SerializedName("child_type")
	val childType: Any,

	@field:SerializedName("beds")
	val beds: Int,

	@field:SerializedName("cancle_policy")
	val canclePolicy: Int,

	@field:SerializedName("status")
	val status: Int,

	@field:SerializedName("min_guests")
	val minGuests: Int,

	@field:SerializedName("city")
	val city: Int,

	@field:SerializedName("contract_image")
	val contractImage: String,

	@field:SerializedName("addedby")
	val addedby: Any,

	@field:SerializedName("latitude")
	val latitude: String,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("min_days")
	val minDays: Int,

	@field:SerializedName("tourism")
	val tourism: Int,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("video")
	val video: Any,

	@field:SerializedName("is_favourite")
	val isFavourite: Boolean,

	@field:SerializedName("title")
	val title: String,

	@field:SerializedName("tourism_static")
	val tourismStatic: Int,

	@field:SerializedName("government")
	val government: Int,

	@field:SerializedName("updated_at")
	val updatedAt: String,

	@field:SerializedName("max_childrens")
	val maxChildrens: Int,

	@field:SerializedName("currency")
	val currency: String,

	@field:SerializedName("checkout")
	val checkout: String,

	@field:SerializedName("address")
	val address: String,

	@field:SerializedName("balacons")
	val balacons: String,

	@field:SerializedName("rate_score")
	val rateScore: Int,

	@field:SerializedName("allow_infants")
	val allowInfants: Int,

	@field:SerializedName("get_keys")
	val getKeys: String,

	@field:SerializedName("guests")
	val guests: Int,

	@field:SerializedName("building_number")
	val buildingNumber: String,

	@field:SerializedName("promotion")
	val promotion: Int
)

data class HotelDlink(

	@field:SerializedName("active")
	val active: Boolean,

	@field:SerializedName("label")
	val label: String,

	@field:SerializedName("url")
	val url: Any
)
