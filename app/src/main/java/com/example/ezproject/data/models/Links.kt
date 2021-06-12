package com.example.ezproject.data.models

import com.google.gson.annotations.SerializedName

data class Links(

	@field:SerializedName("next")
	val next: Any,

	@field:SerializedName("last")
	val last: String,

	@field:SerializedName("prev")
	val prev: Any,

	@field:SerializedName("first")
	val first: String
)