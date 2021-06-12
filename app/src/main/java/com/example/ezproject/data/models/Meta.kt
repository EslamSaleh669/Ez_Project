package com.example.ezproject.data.models

import com.google.gson.annotations.SerializedName

data class Meta(

	@field:SerializedName("path")
	val path: String,

	@field:SerializedName("per_page")
	val perPage: String,

	@field:SerializedName("total")
	val total: Int,

	@field:SerializedName("last_page")
	val lastPage: Int,

	@field:SerializedName("from")
	val from: Int,

	@field:SerializedName("links")
	val links: List<LinksItem>,

	@field:SerializedName("to")
	val to: Int,

	@field:SerializedName("current_page")
	val currentPage: Int
)