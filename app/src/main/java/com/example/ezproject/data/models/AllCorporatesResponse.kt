package com.example.ezproject.data.models

import com.google.gson.annotations.SerializedName

data class AllCorporatesResponse(

	@field:SerializedName("data")
	val data: List<DataItem>,

	@field:SerializedName("meta")
	val meta: Meta,

	@field:SerializedName("links")
	val links: Links
)