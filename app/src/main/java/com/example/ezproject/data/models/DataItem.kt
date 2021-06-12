package com.example.ezproject.data.models

import com.google.gson.annotations.SerializedName

data class DataItem(

	@field:SerializedName("minimum_price")
	val minimumPrice: Int,

	@field:SerializedName("hotel_photo")
	val hotelPhoto: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id")
	val id: Int
)