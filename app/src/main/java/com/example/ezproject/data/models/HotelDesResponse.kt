package com.example.ezproject.data.models

import com.google.gson.annotations.SerializedName

data class HotelDesResponse(

	@field:SerializedName("parent")
	val parent: Int,

	@field:SerializedName("description_en")
	val descriptionEn: Any,

	@field:SerializedName("region_id")
	val regionId: Any,

	@field:SerializedName("description")
	val description: String,

	@field:SerializedName("photo")
	val photo: Any,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("type")
	val type: String,

	@field:SerializedName("updated_at")
	val updatedAt: String,

	@field:SerializedName("taxArrange")
	val taxArrange: Any,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("name_en")
	val nameEn: String,

	@field:SerializedName("status")
	val status: Int
)
