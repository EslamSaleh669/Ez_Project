package com.example.ezproject.data.models

import com.google.gson.annotations.SerializedName

data class DeleteDraftResponse(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
)
